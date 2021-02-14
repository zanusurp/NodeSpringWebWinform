const mongoose = require('mongoose');

//Schema
var commentSchema = mongoose.Schema({
    post:{type:mongoose.Schema.Types.ObjectId, ref:'post', required:true},
    author:{type:mongoose.Schema.Types.ObjectId, ref:'user', required:true},
    parentComment:{type:mongoose.Schema.Types.ObjectId, ref:'comment'},
    text:{type:String, required:[true, 'text is required']},
    isDeleted:{type:Boolean},
    createdAt:{type:Date, default:Date.now},
    updatedAt:{type:Date},

},{
    toObject:{virtuals:true}
});

commentSchema.virtual('childComments')
    .get(function(){return this._childComments;})
    .set(function(value){return this,_childComments;});

const Comment = mongoose.model('comment',commentSchema);
module.exports = Comment;
