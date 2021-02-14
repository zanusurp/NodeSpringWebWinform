const util = {};

util.parseError = function parseError(errors){
    const parsed = {};
    if(errors.name == 'ValidationError'){
        for(var name in errors.errors){
            var ValidationError = errors.errors[name];
            parsed[name] = {message:ValidationError.message};
        }
    }
    else if(errors.code =='11000' && errors.errmsg.indexOf('username')>0){
        parsed.username = { message:'This username already exists' };
    }
    else{
        parsed.unhandled = JSON.stringify(errors);
    }

    return parsed;
}

util.isLoggedin = function(req,res,next){
    if(req.isAuthenticated()){
        next();
    }
    else{
        req.flash('errors',{login:'Please login first'});
        res.redirect('/login');
    }
}
util.noPermission = function(req,res){
    req.flash('errors', {login:"You don't have permission"});
    req.logout();
    res.redirect('/login');
}


util.getPostQueryString = function(req,res,next){
    res.locals.getPostQueryString = function(isAppended = false, overwrites = {}){
        let queryString = '';
        let queryArray = [];
        let page = overwrites.page?overwrites.page:(req.query.page?req.query.page:'');
        let limit = overwrites.limit?overwrites.limit:(req.query.limit?req.query.limit:'');
        let searchType = overwrites.searchType?overwrites.searchType:(req.query.searchType?req.query.searchType:'');
        let searchText  = overwrites.searchText?overwrites.searchText:(req.query.searchText?req.query.searchText:'');


        if(page) queryArray.push('page='+page);
        if(limit) queryArray.push('limit='+limit);
        if(searchType) queryArray.push('searchType='+searchType);
        if(searchText) queryArray.push('searchText='+searchText);
        if(queryArray.length>0) queryString = (isAppended?'&':'?') + queryArray.join('&');
        return queryString;
    }  
    next();
}


module.exports = util;