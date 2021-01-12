var http = require('http');
var server = http.createServer();
var fs = require('fs');

server.on('request',function(req,res){
    console.log('클라이언트 요청이 들어왔습니다.');
    var filename = 'house.png';
    var infile = fs.createReadStream(filename,{flags:'r'});
    var filelength = 0;
    var curlength = 0;

    fs.stat(filename,function(err,states){
        filelength = states.size;
    });
    res.writeHead(200,{"Content-Type":"image/png"});

    infile.on('readable', function(){
        var chunk;
        
        while(null !==(chunk = infile.read())){
            console.log('읽어 들인 데이터 : %d 바이트 ', chunk.length);
            curlength += chunk.length;
            res.write(chunk, 'utf8',function(err){
                console.log('파일 부분 쓰기 완료 : %d , 파일 크기 : %d', curlength,filelength);
                if(curlength >= filelength){
                    res.end();
                }
            });
        }
    });
});
var port = 3000;
server.listen(port,function(err){
    if(err) throw err;
    console.log('서버가 포트 %d에 시작되었습니다.',port);
});