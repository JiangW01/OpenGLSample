precision mediump float;
varying vec2 vTextureCo;
uniform sampler2D uTexture;

void main() {
    vec4 color = texture2D(uTexture, vTextureCo);
    float alpha = max(max(color.r, color.g), color.b);
    gl_FragColor= vec4(color.rgb, alpha);
}
