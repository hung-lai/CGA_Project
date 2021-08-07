package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import cga.exercise.game.*
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*

/**
 * Creates a Mesh object from vertexdata, intexdata and a given set of vertex attributes
 *
 * @param vertexdata plain float array of vertex data
 * @param indexdata  index data
 * @param attributes vertex attributes contained in vertex data
 * @throws Exception If the creation of the required OpenGL objects fails, an exception is thrown
 *
 * Created by Fabian on 16.09.2017.
 */
class Mesh(vertices: FloatArray, indices: IntArray, attributes: Array<VertexAttribute>, var material:Material? = null) {
    //private data
    private var vao = 0
    private var vbo = 0
    private var ibo = 0
    private var indexcount = indices.size

    init {

        vao = glGenVertexArrays()                                                                                       //generiert und gibt eine VAO ID zurück
        glBindVertexArray(vao)                                                                                          //Aktiviert den Buffer

        vbo = glGenBuffers()                                                                                            //generiert und gibt eine VBO ID zurück
        glBindBuffer(GL_ARRAY_BUFFER, vbo)                                                                              //Aktiviert den Buffer
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)                                                         //lädt den Buffer auf die GPU hoch


        ibo = glGenBuffers()                                                                                            //generiert und gibt eine IBO ID zurück
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo)                                                                      //Aktiviert den Buffer
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)


        glEnableVertexAttribArray(0)                                                                              //sagt OpenGl welche Vertexattribute aktiv sind (Position)
        //glVertexAttribPointer(0, 3, GL_FLOAT, false, 24, 0)                             //wo sind welche Daten

        glEnableVertexAttribArray(1)                                                                              //sagt OpenGl welche Vertexattribute aktiv sind (Farbe)
        //glVertexAttribPointer(1, 3, GL_FLOAT, false, 24, 12)                            //wo sind welche Daten

        for(m in attributes){
            GL20.glEnableVertexAttribArray(m.index)
            glVertexAttribPointer(m.index, m.n, m.type, false, m.stride, m.offset)
        }
        /*
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 32, 0)                                                             //sagt OpenGl welche Vertexattribute aktiv sind (Textur)
        glEnableVertexAttribArray(1)                                                                                    //sagt OpenGl welche Vertexattribute aktiv sind (Textur)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 32, 12)
        glEnableVertexAttribArray(2)                                                                                    //sagt OpenGl welche Vertexattribute aktiv sind (Farbe)
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 32, 20)
*/
        glBindVertexArray(0)
        //glDeleteBuffers(vbo)
        //indexcount = indices.size
    }

    /**
     * renders the mesh
     */
    fun render() {
        glBindVertexArray(vao)
        GL11.glDrawElements(GL_TRIANGLES, indexcount, GL_UNSIGNED_INT, 0)
        glBindVertexArray(0)
        // call the rendering method every frame
    }
    fun render(shader: ShaderProgram) {
        material?.bind(shader)                                                                                          //?. = ist Material vorhanden, wenn nicht material=null
        render()
    }


    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */

    fun cleanup() {
        if (ibo != 0) glDeleteBuffers(ibo)
        if (vbo != 0) glDeleteBuffers(vbo)
        if (vao != 0) glDeleteVertexArrays(vao)
    }
}