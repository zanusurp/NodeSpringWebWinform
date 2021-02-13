//Model Post.js
const mongoose = require('mongoose');

//schema
const postSchema = mongoose.Schema({
    title:{type:String, required:true},
    body:{type:String, required:true},
    author:{type:mongoose.Schema.Types.ObjectId, ref:'user',required:true},
    createdAt:{type:Date, default:Date.now},
    updatedAt:{type:Date}
});

// export
const Post = mongoose.model('post',postSchema);
module.exports = Post;