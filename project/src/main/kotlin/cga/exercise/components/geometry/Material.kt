package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.Vector2f

class Material(var diff: Texture2D,
               var emit: Texture2D,
               var spec: Texture2D,
               var shininess: Float = 50.0f,
               var tcMultiplier : Vector2f = Vector2f(1.0f)){

    fun bind(shaderProgram: ShaderProgram) {
        emit.bind(0) //bind aus Texture2d
        diff.bind(1)
        spec.bind(2)

        shaderProgram.setUniform("emit",0)                                                                  //emit wird an Shader weitergegeben
        shaderProgram.setUniform("diff",1)
        shaderProgram.setUniform("spec",2)
        shaderProgram.setUniform("tcMultiplier", tcMultiplier)                                                    //tcMultiplier an shader weitergeben //auswirkung auf die wiederholung von Textures
        shaderProgram.setUniform("shininess", shininess)
    }
}