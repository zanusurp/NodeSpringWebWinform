const express = require('express');
const router= express.Router();
const Post = require('../model/Post');
const User = require('../model/User');
const Comment = require('../model/Comment');
const util = require('../util');

//index
router.get('/', async function(req,res){
    let page = Math.max(1, parseInt(req.query.page));
    let limit = Math.max(1, parseInt(req.query.limit));
    page = !isNaN(page)?page:1;
    limit = !isNaN(limit)?limit:10;

    

    let skip = (page-1)*limit;
    let maxPage = 0;
    let searchQuery = await createSearchQuery(req.query);
    let posts = [];
    if(searchQuery){
        let count = await Post.countDocuments(searchQuery);
        maxPage = Math.ceil(count/limit);
        posts = await Post.find(searchQuery)
                .populate('author')
                .sort('-createdAt')
                .skip(skip)
                .limit(limit)
                .exec();
    }

    res.render('posts/index', {
        posts:posts,
        currentPage:page,
        maxPage:maxPage,
        limit:limit,
        searchType:req.query.searchType,
        searchText:req.query.searchText
    });
});

//new
router.get('/new', util.isLoggedin, function(req,res){
    const post = req.flash('post')[0] || {};
    const errors = req.flash('errors')[0] || {};
    res.render('posts/new',{post:post, errors:errors});
});

//create
router.post('/',util.isLoggedin,function(req,res){
    req.body.author = req.user._id;
    Post.create(req.body, function(err, post){
        if(err){
            req.flash('post',req.body);
            req.flash('errors',util.parseError(err));
            return res.redirect('/posts/new'+res.locals.getPostQueryString());
        }
        res.redirect('/posts'+res.locals.getPostQueryString(false,{page:1, searchText:''}));
    });
});

//show
router.get('/:id', function(req,res){
    var commentForm = req.flash('commentForm')[0] || {_id: null, form: {}};
    var commentError = req.flash('commentError')[0] || { _id:null, parentComment: null, errors:{}};
    Promise.all([
        Post.findOne({_id:req.params.id}).populate({path:'author', select:'username'}),
        Comment.find({post:req.params.id}).sort('createdAt').populate({path:'author',select:'username'})
    ])
    .then(([post, comments])=>{
        res.render('posts/show',{post:post, comments:comments, commentForm:commentForm, commentError:commentError});
    })
    .catch((err)=>{
        console.log('err:', err);
        return res.json(err);
    });
});
//edit
router.get('/:id/edit',util.isLoggedin, checkPermission, function(req,res){
    const post = req.flash('post')[0];
    const errors = req.flash('errors')[0] || {};
    if(!post){
        Post.findOne({_id:req.params.id}, function(err, post){
            if(err) return res.json(err);
            res.render('posts/edit', {post: post, errors:errors});
        });
    }else{
        post._id = req.params.id;
        res.render('posts/edit', {post:post, errors:errors});
        
    }
    
});
//update
router.put('/:id',util.isLoggedin, checkPermission, function(req, res){
    req.body.updatedAt = Date.now();
    Post.findOneAndUpdate({_id:req.params.id}, req.body, {runValidators:true}, function(err, post){
        if(err){
            req.flash('post',req.body);
            req.flash('errors',util.parseError(err));
            return res.redirect('/posts/'+req.params.id+'/edit'+res.locals.getPostQueryString());
        }
        res.redirect('/posts/'+req.params.id+res.locals.getPostQueryString());
    });
});
//delete
router.delete('/:id',util.isLoggedin, checkPermission, function(req,res){
    Post.deleteOne({_id:req.params.id}, function(err){
        if(err) return res.json(err);
        res.redirect('/posts'+res.locals.getPostQueryString());
    });
});
module.exports = router;

function checkPermission(req,res,next){
    Post.findOne({_id:req.params.id}, function(err, post){
        if(err) return res.json(err);
        if(post.author != req.user.id) return util.noPermission(req, res);

        next();
    });
}
async function createSearchQuery(queries){
    let searchQuery = {};
    if(queries.searchType && queries.searchText && queries.searchText.length >=3){
        let searchType = queries.searchType.toLowerCase().split(',');
        let postQueries = [];

        if(searchType.indexOf('title') >= 0){
            postQueries.push({title:{$regex:new RegExp(queries.searchText,'i')}});
        }if(searchType.indexOf('body')>=0){
            postQueries.push({body:{$regex:new RegExp(queries.searchText,'i')}});
        }if(searchType.indexOf('author!')>=0){
            let user = await User.findOne({username:queries.searchText}).exec();
            if(user) postQueries.push({author:user._id});
        }else if(searchType.indexOf('author')>=0){
            let users = await User.find({username:{$regex:new RegExp(queries.searchText,'i')}}).exec();
            let userIds = [];
            for(let user of users){
                userIds.push(user._id);
            }
            if(userIds.length>0) postQueries.push({author:{$in:userIds}});
        }
        if(postQueries.length>0) searchQuery = {$or:postQueries};
        else searchQuery = null;
    }
    return searchQuery;
}