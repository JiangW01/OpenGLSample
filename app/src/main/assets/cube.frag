

//纯色三角形
precision mediump float;
uniform vec4 vColor;
void main(){
    gl_FragColor = vColor;
}

//彩色三角形
//precision mediump float;
//varying vec4 vColor;
//void main(){
//    gl_FragColor = vColor;
//}