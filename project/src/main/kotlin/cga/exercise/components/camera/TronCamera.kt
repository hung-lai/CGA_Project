package cga.exercise.components.camera

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f
import org.joml.Math

class TronCamera(var fieldOfView : Float = Math.toRadians(90.0f), var aspectRatio : Float = 16.0f/9.0f, var nearPlane : Float = 0.1f, var farPlane : Float = 100.0f):ICamera, Transformable() {
    override fun getCalculateViewMatrix(): Matrix4f {
        val eye = getWorldPosition()
        val center = getWorldPosition().sub(getWorldZAxis())
        val up = getWorldYAxis()

        return Matrix4f().lookAt(eye,center,up)
    }

    override fun getCalculateProjectionMatrix(): Matrix4f {
        return Matrix4f().perspective(fieldOfView,aspectRatio,nearPlane,farPlane)
    }

    override fun bind(shader: ShaderProgram) {
        shader.setUniform("view_matrix", getCalculateViewMatrix(), false)
        shader.setUniform("projection_matrix", getCalculateProjectionMatrix(), false)
    }
}