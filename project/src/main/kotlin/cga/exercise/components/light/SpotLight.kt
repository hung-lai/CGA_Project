package cga.exercise.components.light

import cga.exercise.components.shader.ShaderProgram
import org.joml.*

open class SpotLight (lightPos: Vector3f, lightCol: Vector3f, attParam: Vector3f = Vector3f(0.5f, 0.05f, 0.01f), var angle: Vector2f = Vector2f(Math.toRadians(15.0f), Math.toRadians(30.0f))): ISpotLight, PointLight(lightPos,lightCol,attParam){
    override fun bind(shaderProgram: ShaderProgram, name: String, viewMatrix: Matrix4f){
        super.bind(shaderProgram,name)
        shaderProgram.setUniform(name + "LightAngle", angle)
        shaderProgram.setUniform(name + "LightDir", getWorldZAxis().negate().mul(Matrix3f(viewMatrix)))
    }

}