

//片元着色器代码
precision mediump float;
uniform vec4 vColor;
void main(){
    gl_FragColor = vColor;
}