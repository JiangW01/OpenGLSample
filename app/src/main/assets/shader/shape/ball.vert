


attribute vec3 aPosition;
uniform mat4 uMVPMatrix;
varying vec3 vPosition;
uniform vec4 uAmbient;
varying vec4 vAmbient;//用于传递给片元着色器的环境光分量
void main(){
   gl_Position = uMVPMatrix * vec4(aPosition,1);
   //将顶点的位置传给片元着色器
   vPosition = aPosition;//将原始顶点位置传递给片元着色器
   //将的环境光分量传给片元着色器
   vAmbient = vec4(uAmbient);

}

