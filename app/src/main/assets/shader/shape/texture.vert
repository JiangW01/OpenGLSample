
attribute vec3 vPosition;
uniform mat4 uMVPMatrix;
attribute vec2 aTextureCoordinates;
varying vec2 vTextureCoordinates;
void main(){
    gl_Position = uMVPMatrix*vec4(vPosition,1);
    vTextureCoordinates = aTextureCoordinates;
}

