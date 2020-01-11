precision mediump float;
varying vec2 vTextureCo;//顶点着色器传递多来的纹理顶点坐标
uniform sampler2D uTexture;//纹理
void main() {
    gl_FragColor = texture2D( uTexture, vTextureCo);
}