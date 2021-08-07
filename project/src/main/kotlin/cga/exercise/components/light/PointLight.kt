package cga.exercise.components.light

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f

open class PointLight(var lightPos: Vector3f = Vector3f(), var lightColor: Vector3f = Vector3f(), var attParam: Vector3f = Vector3f(1.0f, 0.5f, 0.1f)): IPointLight, Transformable() {

    init{
        translateGlobal(lightPos)
    }
    override fun bind(shaderProgram: ShaderProgram, name: String){
        shaderProgram.setUniform(name + "LightPos", getWorldPosition())
        shaderProgram.setUniform(name + "LightCol", lightColor)
        shaderProgram.setUniform(name + "LightAttParam", attParam)
    }
}