precision highp float;//高精度
precision highp int;//高精度
attribute vec4 aVertexCo; //顶点向量
attribute vec2 aTextureCo;//纹理顶点向量

uniform mat4 uVertexMatrix;//顶点矩阵
uniform mat4 uTextureMatrix;//纹理矩阵

varying vec2 vTextureCo;//传递给片元着色器的纹理向量

void main(){
    gl_Position = uVertexMatrix*aVertexCo;
    vTextureCo = (uTextureMatrix*vec4(aTextureCo,0,1)).xy;
}