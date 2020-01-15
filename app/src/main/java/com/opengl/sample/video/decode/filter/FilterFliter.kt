package com.opengl.sample.video.decode.filter

/**
 * 作者 ：wangJiang
 * 时间 ：2019/7/29
 * 描述 ：
 */


class FilterFliter : BaseFilter(
    BASE_VERTEX_SHADER,FILTER_FRAGMENT_SHADER
){
  companion object{
      const val  FILTER_FRAGMENT_SHADER = """
        #extension GL_OES_EGL_image_external : require
        precision highp float;
        uniform samplerExternalOES texture;
        varying highp vec2 v_TexCoordinate;
        void main () {
            vec4 color = texture2D(texture, v_TexCoordinate);
            float alpha = max(max(color.r, color.g), color.b);
            gl_FragColor = vec4(color.rgb, alpha);
        }
        """
  }
}