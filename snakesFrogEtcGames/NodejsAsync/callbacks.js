
//async
//https://www.youtube.com/watch?v=PoRJizFvM7s
const posts = [
    {title:'Post One', body:'This is post one'},
    {title:'Post Two', body:'This is post two'}
];

function getPosts(){
    setTimeout(function() {
        let output = '';
        posts.forEach((post, index)=>{
            output += `<li>${post.title}</li>`;
        });
        document.body.innerHTML = output;
    },1000);
}

function createPost(post, callback){
    setTimeout(()=>{
        posts.push(post);
        callback();
    },2000);
}



createPost({title:'Post Three',body:'The is post three'},getPosts);