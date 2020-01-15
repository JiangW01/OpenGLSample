

attribute vec4 aPosition;//顶点坐标向量
void main(){
    gl_Position = aPosition;
    //定义点的像素大小
    gl_PointSize = 20.0;
}