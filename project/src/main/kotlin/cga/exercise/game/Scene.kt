package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import cga.framework.OBJLoader
import cga.framework.OBJLoader.loadOBJ
import org.joml.*
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL30.*
import java.lang.Math.*
import kotlin.math.sin

public class Scene(private val window: GameWindow) {
    //private val staticShader: ShaderProgram = ShaderProgram("assets/shaders/simple_vert.glsl", "assets/shaders/simple_frag.glsl")
    private val staticShader1: ShaderProgram = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")
    private var boden = Renderable() //aka Günther
    private var cycle = ModelLoader.loadModel("assets/Light Cycle/Light Cycle/HQ_Movie cycle.obj", Math.toRadians(-90.0f),Math.toRadians(90.0f),0.0f)?: throw IllegalAccessException("Da is was nicht okay :(") //aka Dieter
    private var meshBoden : Mesh
    //private var meshKugel : Mesh
    private var kamera = TronCamera()
    private var kameraOben = TronCamera()
    private var kameraTP = TronCamera()
    private var kameraFP = TronCamera()
    //scene setup
    private var pointLight : PointLight
    private var pointLight2 : PointLight
    private var spotLight : SpotLight

    private var oldMousePosX : Double = -1.0
    private var oldMousePosY : Double = -1.0
    private var bool: Boolean = false
    init {

        //initial opengl state
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow() //schwarz
        glDisable(GL_CULL_FACE); GLError.checkThrow()
        glEnable(GL_CULL_FACE)
        glFrontFace(GL_CCW); GLError.checkThrow()
        glCullFace(GL_BACK); GLError.checkThrow()
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()

        //load an object and create a mesh
        val res = loadOBJ("assets/models/ground.obj")
        //Get the first mesh of the first object
        val objMesh: OBJLoader.OBJMesh = res.objects[0].meshes[0]
        //Create the mesh
        val stride = 8 * 4
        val attrPos = VertexAttribute(0,3, GL_FLOAT, stride, 0) //position
        val attrTC = VertexAttribute(1,2, GL_FLOAT, stride, 3 * 4) //textureCoordinate
        val attrNorm = VertexAttribute(2,3, GL_FLOAT, stride, 5 * 4) //normalval
        val vertexAttributes = arrayOf<VertexAttribute>(attrPos, attrTC, attrNorm)
        //meshBoden = Mesh(objMesh.vertexData, objMesh.indexData, vertexAttributes, bodenMaterial)

        val texture_emit = Texture2D("assets/textures/ground_emit.png", true)
        val texture_diff = Texture2D("assets/textures/ground_diff.png",true)
        val texture_spec = Texture2D("assets/textures/ground_spec.png",true)

        //texture_emit.setTexParams(GL_NEAREST,GL_NEAREST, GL_NEAREST_MIPMAP_NEAREST, GL_NEAREST)                       //moire-Effekt
        texture_emit.setTexParams(GL_REPEAT,GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)                              //GL_Repeat werte hinterm Komma werden genutzt. Linear =
        texture_diff.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        texture_spec.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)

        val bodenMaterial = Material(texture_diff, texture_emit, texture_spec, 60.0f, Vector2f(1.0f, 1.0f))   //neuen Boden erstellen mit unseren Werten

        meshBoden = Mesh(objMesh.vertexData, objMesh.indexData, vertexAttributes, bodenMaterial)                        //bodenMaterial wird benötigt
        boden.list.add(meshBoden)

        pointLight = PointLight(kamera.getWorldPosition(), Vector3f(1f,1f,0f))
        pointLight2 = PointLight(Vector3f(20.0f, 4.0f,20.0f),Vector3f(1.0f,1.0f,1.0f), Vector3f(1.0f,0.5f,0.1f))
        spotLight = SpotLight(Vector3f(0.0f, 1.0f,-2.0f), Vector3f(1.0f))

        cycle.scaleLocal(Vector3f(0.8f))
        pointLight.translateLocal(Vector3f(0.0f, 4.0f, 0.0f))
        spotLight.rotateLocal(Math.toRadians(-10.0f), Math.PI.toFloat(), 0.0f)

        kamera = kameraOben

        kameraOben.rotateLocal(Math.toRadians(-90.0f), 0.0f, 0.0f)
        kameraOben.translateLocal(Vector3f(0.0f,0.0f,25.0f))

        kameraTP.rotateLocal(Math.toRadians(-35f), 0f, 0f)
        kameraTP.translateLocal(Vector3f(0f,0f,4f))

        kameraFP.rotateLocal(Math.toRadians(-10f),0f,0f)
        kameraFP.translateLocal(Vector3f(0f,2f,-1f))

        kameraTP.parent = cycle
        kameraFP.parent = cycle
        spotLight.parent = cycle
        pointLight.parent = cycle
    }

    fun render(dt: Float, t: Float) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        staticShader1.setUniform("sceneColor", Vector3f(1.0f,1.0f,1.0f))
        kamera.bind(staticShader1)
        boden.render(staticShader1)

        staticShader1.setUniform("sceneColor",Vector3f(abs(sin(t/1)),abs(sin(t/3)),abs(sin(t/2))))          //statemaschine
        cycle.render(staticShader1)
        pointLight.bind(staticShader1, "cyclePoint")
        pointLight2.bind(staticShader1, "ecke")
        spotLight.bind(staticShader1, "cycleSpot", kamera.getCalculateViewMatrix())
        pointLight.lightColor = Vector3f(abs(sin(t/1)),abs(sin(t/3)),abs(sin(t/2)))
    }

    fun update(dt: Float, t: Float) {
        if(window.getKeyState(GLFW_KEY_W)){
            cycle.translateLocal(Vector3f(0.0f, 0.0f, -25*dt))

            if(window.getKeyState(GLFW_KEY_A)) {
                cycle.rotateLocal(0.0f, Math.toRadians(100* dt), 0.0f)
            }
            if(window.getKeyState(GLFW_KEY_D)){
                cycle.rotateLocal(0.0f, Math.toRadians(-100 * dt),0.0f)
            }
        }
        if(window.getKeyState(GLFW_KEY_S)){
            cycle.translateLocal(Vector3f(0.0f, 0.0f,5 * dt))

            if(window.getKeyState(GLFW_KEY_D)){
                cycle.rotateLocal(0.0f, Math.toRadians(60 * dt), 0.0f)
            }
            if(window.getKeyState(GLFW_KEY_A)){
                cycle.rotateLocal(0.0f, Math.toRadians(-60 * dt), 0.0f)
            }
        }
    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {
        if(window.getKeyState(GLFW_KEY_V)) {
            if(kamera == kameraOben){
                kamera = kameraTP
            }else{
                if(kamera == kameraTP){
                kamera = kameraFP
            }else{
                kamera = kameraOben

                }
            }
        }
    }

    fun onMouseMove(xpos: Double, ypos: Double) {

        val x: Double = xpos - oldMousePosX
        //val y: Double = ypos - oldMousePosY

        oldMousePosX = xpos
        //oldMousePosY = ypos

        if(bool){
            kamera.rotateAroundPoint(0.0f, Math.toRadians(x.toFloat() * 0.002f), 0.0f, Vector3f(0.0f))
        }
        bool = true

    }

    fun cleanup() {}
}
