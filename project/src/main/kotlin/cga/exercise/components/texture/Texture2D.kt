package cga.exercise.components.texture

import cga.framework.GLError
import cga.framework.GLError.checkEx
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.EXTTextureFilterAnisotropic
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL30.glGenerateMipmap
import org.lwjgl.stb.STBImage
import java.nio.ByteBuffer


/**
 * Created by Fabian on 16.09.2017.
 */
class Texture2D(imageData: ByteBuffer, width: Int, height: Int, genMipMaps: Boolean): ITexture{
    private var texID: Int = -1
        private set

    init {
        try {
            processTexture(imageData, width, height, genMipMaps)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }
    companion object {
        //create texture from file
        //don't support compressed textures for now
        //instead stick to pngs
        operator fun invoke(path: String, genMipMaps: Boolean): Texture2D {
            val x = BufferUtils.createIntBuffer(1)
            val y = BufferUtils.createIntBuffer(1)
            val readChannels = BufferUtils.createIntBuffer(1)
            //flip y coordinate to make OpenGL happy
            STBImage.stbi_set_flip_vertically_on_load(true)
            val imageData = STBImage.stbi_load(path, x, y, readChannels, 4)
                    ?: throw Exception("Image file \"" + path + "\" couldn't be read:\n" + STBImage.stbi_failure_reason())

            try {
                return Texture2D(imageData, x.get(), y.get(), genMipMaps)
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
                throw ex
            } finally {
                STBImage.stbi_image_free(imageData)
            }
        }
    }

    override fun processTexture(imageData: ByteBuffer, width: Int, height: Int, genMipMaps: Boolean) {
        texID = glGenTextures()                                                                                         //Rexture ID generieren
        glBindTexture(GL_TEXTURE_2D, texID)                                                                             //Texture an ID binden
        glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData)       //Textur laden und hochladen //RGBA = RGB + Tiefe
        if(genMipMaps){                                                                                                 //bessere Darstellung der Kästen die weiter weg sind
            glGenerateMipmap(GL_TEXTURE_2D)
        }
        glBindTexture(GL_TEXTURE_2D,0)                                                                            //unbind wie mit VAO
    }

    override fun setTexParams(wrapS: Int, wrapT: Int, minFilter: Int, magFilter: Int) {
        glBindTexture(GL_TEXTURE_2D, texID)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S,wrapS)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter)                                                //minification FIlter = mehrere Texel auf 1 Pixel
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter)                                                //magnification Filter = 1 Texel auf mehrere Pixel

        glTexParameterf(GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 16.0f)          //hintergrund wird klarer dargestellt

        glBindTexture(GL_TEXTURE_2D, 0)                                                                           //unbind wie mit vao
    }

    override fun bind(textureUnit: Int) {
        glActiveTexture(GL_TEXTURE0 + textureUnit)                                                               //wir fangen bei 0 an und packen textureUnit drauf
        glBindTexture(GL_TEXTURE_2D, texID)                                                                             //an ID binden
    }

    override fun unbind() {
         glBindTexture(GL_TEXTURE_2D, 0)
    }

    override fun cleanup() {
        unbind()
        if (texID != 0) {
            GL11.glDeleteTextures(texID)
            texID = 0
        }
    }
}