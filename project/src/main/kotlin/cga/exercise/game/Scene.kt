package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.light.SpotLight
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.OBJLoader
import cga.framework.OBJLoader.loadOBJ
import org.joml.*
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL30.*
import java.lang.Math.*
import kotlin.math.sin

public class Scene(private val window: GameWindow) {
    private val staticShader1: ShaderProgram = ShaderProgram("project/assets/shaders/tron_vert.glsl", "project/assets/shaders/tron_frag.glsl")
    private var boden = Renderable()
    private var meshBoden : Mesh
    private var kamera = TronCamera()
    private var kameraOben = TronCamera()
    private var kameraTP = TronCamera()
    private var kameraFP = TronCamera()
    //scene setup
    //private var pointLight : PointLight
    //private var pointLight2 : PointLight
    private var spotLightCar1FR : SpotLight
    private var spotLightCar1FL : SpotLight
    private var spotLightCar1BR : SpotLight
    private var spotLightCar1BL : SpotLight

    private var spotLightCar2FR : SpotLight
    private var spotLightCar2FL : SpotLight
    private var spotLightCar2BR : SpotLight
    private var spotLightCar2BL : SpotLight

    private var oldMousePosX : Double = -1.0
    private var oldMousePosY : Double = -1.0
    private var bool: Boolean = false

    //private var cp1 = Checkpoint()

    private var i = 0
    private var drehungFahrzeug2 = 0
    private var drehungFahrzeug1 = 0
    private var runde = 0
    private var runde2 = 0
    private var aktuellePosition = Vector3f(0f,0f,0f)
    private var aktuellePosition2 = Vector3f(0f,0f,0f)
    private var aktuelleRotation = Vector3f(0f,0f,0f)
    private var aktuelleRotation2 = Vector3f(0f,0f,0f)
    private var h = 0
    private var h1 = 0
    private var speed = -1f
    private var speed2 = -1f
    //private var trackPos = Vector3f(0f,0f,0f)

    private var carMesh : Mesh
    private var car1 = Renderable()
    private var car2Mesh : Mesh
    private var car2 = Renderable()

    init {

        //initial opengl state
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f); GLError.checkThrow() //schwarz
        glDisable(GL_CULL_FACE); GLError.checkThrow()
        glEnable(GL_CULL_FACE)
        glFrontFace(GL_CCW); GLError.checkThrow()
        glCullFace(GL_BACK); GLError.checkThrow()
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()

        //load an object and create a mesh
        val res = loadOBJ("project/assets/models/track1.obj")
        //Get the first mesh of the first object
        val objMesh: OBJLoader.OBJMesh = res.objects[0].meshes[0]
        //Create the mesh
        val stride = 8 * 4
        val attrPos = VertexAttribute(0,3, GL_FLOAT, stride, 0) //position
        val attrTC = VertexAttribute(1,2, GL_FLOAT, stride, 3 * 4) //textureCoordinate
        val attrNorm = VertexAttribute(2,3, GL_FLOAT, stride, 5 * 4) //normalval
        val vertexAttributes = arrayOf<VertexAttribute>(attrPos, attrTC, attrNorm)
        //meshBoden = Mesh(objMesh.vertexData, objMesh.indexData, vertexAttributes, bodenMaterial)

        val texture_emit_track = Texture2D("project/assets/textures/track.png",true)
        val texture_diff_track = Texture2D("project/assets/textures/ground_diff.png",true)
        val texture_spec_track = Texture2D("project/assets/textures/ground_spec.png",true)
        val texture_emit_car1 = Texture2D("project/assets/textures/car1.png",true)
        val texture_diff_car1 = Texture2D("project/assets/textures/ground_diff.png",true)
        val texture_spec_car1 = Texture2D("project/assets/textures/ground_spec.png",true)
        val texture_emit_car2 = Texture2D("project/assets/textures/car2.png",true)
        val texture_diff_car2 = Texture2D("project/assets/textures/ground_diff.png",true)
        val texture_spec_car2 = Texture2D("project/assets/textures/ground_spec.png",true)

        //texture_emit.setTexParams(GL_NEAREST,GL_NEAREST, GL_NEAREST_MIPMAP_NEAREST, GL_NEAREST)                       //moire-Effekt
        texture_emit_track.setTexParams(GL_REPEAT,GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)                              //GL_Repeat werte hinterm Komma werden genutzt. Linear =
        texture_diff_track.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        texture_spec_track.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        texture_emit_car1.setTexParams(GL_REPEAT,GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)                              //GL_Repeat werte hinterm Komma werden genutzt. Linear =
        texture_diff_car1.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        texture_spec_car1.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        texture_emit_car2.setTexParams(GL_REPEAT,GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)                              //GL_Repeat werte hinterm Komma werden genutzt. Linear =
        texture_diff_car2.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        texture_spec_car2.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)

        val carMaterial = Material(texture_diff_car1, texture_emit_car1, texture_spec_car1, 60.0f, Vector2f(1.0f, 1.0f))
        val resCar = OBJLoader.loadOBJ("project/assets/car/car1.obj")
        val carObj = resCar.objects[0].meshes[0]
        carMesh = Mesh(carObj.vertexData, carObj.indexData, vertexAttributes, carMaterial)
        car1.list.add(carMesh)
        val car2Material = Material(texture_diff_car2, texture_emit_car2, texture_spec_car2, 60.0f, Vector2f(1.0f, 1.0f))
        val resCar2 = OBJLoader.loadOBJ("project/assets/car/car2.obj")
        val car2Obj = resCar.objects[0].meshes[0]
        car2Mesh = Mesh(carObj.vertexData, car2Obj.indexData, vertexAttributes, car2Material)
        car2.list.add(car2Mesh)

        val bodenMaterial = Material(texture_diff_track, texture_emit_track, texture_spec_track, 60.0f, Vector2f(1.0f, 1.0f))   //neuen Boden erstellen mit unseren Werten

        meshBoden = Mesh(objMesh.vertexData, objMesh.indexData, vertexAttributes, bodenMaterial)                        //bodenMaterial wird benötigt
        boden.list.add(meshBoden)

        //pointLight = PointLight(kamera.getWorldPosition(), Vector3f(1f,1f,0f))
        //pointLight2 = PointLight(Vector3f(20.0f, 4.0f,20.0f),Vector3f(1.0f,1.0f,1.0f), Vector3f(1.0f,0.5f,0.1f))
        spotLightCar1FR = SpotLight(Vector3f(1.2f, 1.0f,-2.0f), Vector3f(1.0f))
        spotLightCar1FL = SpotLight(Vector3f(-1.2f, 1.0f,-2.0f), Vector3f(1.0f))
        spotLightCar1BR = SpotLight(Vector3f(1.2f, 1.0f,1.0f), Vector3f(1.0f,0.0f,0.0f))
        spotLightCar1BL = SpotLight(Vector3f(-1.2f, 1.0f,1.0f), Vector3f(1.0f,0.0f,0.0f))

        spotLightCar2FR = SpotLight(Vector3f(1.2f, 1.0f,-2.0f), Vector3f(1.0f))
        spotLightCar2FL = SpotLight(Vector3f(-1.2f, 1.0f,-2.0f), Vector3f(1.0f))
        spotLightCar2BR = SpotLight(Vector3f(1.2f, 1.0f,1.0f), Vector3f(1.0f,0.0f,0.0f))
        spotLightCar2BL = SpotLight(Vector3f(-1.2f, 1.0f,1.0f), Vector3f(1.0f,0.0f,0.0f))

        car1.scaleLocal(Vector3f(0.8f))
        car1.translateLocal(Vector3f(-117.0f,1.002f, -45.45f))

        car2.scaleLocal(Vector3f(0.8f))
        car2.translateLocal(Vector3f(-105.0f,-1.248f, -45.45f))

        //pointLight.translateLocal(Vector3f(0.0f, 4.0f, 0.0f))
        spotLightCar1FR.rotateLocal(Math.toRadians(-10.0f), Math.PI.toFloat(), 0.0f)
        spotLightCar1FL.rotateLocal(Math.toRadians(-10.0f), Math.PI.toFloat(), 0.0f)
        spotLightCar1BR.rotateLocal(Math.toRadians(-170.0f), Math.PI.toFloat(), 0.0f)
        spotLightCar1BL.rotateLocal(Math.toRadians(-170.0f), Math.PI.toFloat(), 0.0f)

        spotLightCar2FR.rotateLocal(Math.toRadians(-10.0f), Math.PI.toFloat(), 0.0f)
        spotLightCar2FL.rotateLocal(Math.toRadians(-10.0f), Math.PI.toFloat(), 0.0f)
        spotLightCar2BR.rotateLocal(Math.toRadians(-170.0f), Math.PI.toFloat(), 0.0f)
        spotLightCar2BL.rotateLocal(Math.toRadians(-170.0f), Math.PI.toFloat(), 0.0f)

        kamera = kameraOben

        kameraOben.rotateLocal(Math.toRadians(-90.0f), 0.0f, 0.0f)
        kameraOben.translateLocal(Vector3f(0.0f,0.0f,75.0f))

        kameraTP.rotateLocal(Math.toRadians(-35f), 0f, 0f)
        kameraTP.translateLocal(Vector3f(0f,0f,4f))

        kameraFP.rotateLocal(Math.toRadians(-10f),0f,0f)
        kameraFP.translateLocal(Vector3f(0f,2f,-1f))

        kameraTP.parent = car1
        kameraFP.parent = car1
        spotLightCar2FR.parent = car2
        spotLightCar2FL.parent = car2
        spotLightCar2BR.parent = car2
        spotLightCar2BL.parent = car2
        spotLightCar1FR.parent = car1
        spotLightCar1FL.parent = car1
        spotLightCar1BR.parent = car1
        spotLightCar1BL.parent = car1


        //car1.getPosition()


    }

    fun render(dt: Float, t: Float) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        staticShader1.setUniform("sceneColor", Vector3f(1.0f,1.0f,1.0f))
        kamera.bind(staticShader1)
        boden.render(staticShader1)

        //staticShader1.setUniform("sceneColor",Vector3f(abs(sin(t/1)),abs(sin(t/3)),abs(sin(t/2))))          //statemaschine

        //pointLight.bind(staticShader1, "cyclePoint")
        //pointLight2.bind(staticShader1, "ecke")

        spotLightCar1FR.bind(staticShader1, "carOneSpotFR", kamera.getCalculateViewMatrix())
        spotLightCar1FL.bind(staticShader1, "carOneSpotFL", kamera.getCalculateViewMatrix())
        spotLightCar1BR.bind(staticShader1, "carOneSpotBR", kamera.getCalculateViewMatrix())
        spotLightCar1BL.bind(staticShader1, "carOneSpotBL", kamera.getCalculateViewMatrix())
        car1.render(staticShader1)
        car2.render(staticShader1)
        spotLightCar2FR.bind(staticShader1, "carTwoSpotFR", kamera.getCalculateViewMatrix())
        spotLightCar2FL.bind(staticShader1, "carTwoSpotFL", kamera.getCalculateViewMatrix())
        spotLightCar2BR.bind(staticShader1, "carTwoSpotBR", kamera.getCalculateViewMatrix())
        spotLightCar2BL.bind(staticShader1, "carTwoSpotBL", kamera.getCalculateViewMatrix())

        //pointLight.lightColor = Vector3f(abs(sin(t/1)),abs(sin(t/3)),abs(sin(t/2)))
    }

    fun update(dt: Float, t: Float) {
        if(window.getKeyState(GLFW_KEY_W)){
            car1.translateLocal(Vector3f(0.0f, 0.0f, speed*dt))
            speed--
            //if(window.getKeyState(GLFW_KEY_A)) {
            //    car1.rotateLocal(0.0f, Math.toRadians(100* dt), 0.0f)
            //}
            //if(window.getKeyState(GLFW_KEY_D)){
            //    car1.rotateLocal(0.0f, Math.toRadians(-100 * dt),0.0f)
            //}
        }else{
            if(speed!=0f){
                car1.translateLocal(Vector3f(0.0f, 0.0f, speed*dt))
                speed++
            }
        }
        if(window.getKeyState(GLFW_KEY_S)){
            car1.translateLocal(Vector3f(0.0f, 0.0f,50 * dt))

            //if(window.getKeyState(GLFW_KEY_D)){
            //    car1.rotateLocal(0.0f, Math.toRadians(60 * dt), 0.0f)
            //}
            //if(window.getKeyState(GLFW_KEY_A)){
            //    car1.rotateLocal(0.0f, Math.toRadians(-60 * dt), 0.0f)
            //}
        }
        if(window.getKeyState(GLFW_KEY_UP)){
            car2.translateLocal(Vector3f(0.0f, 0.0f, speed2*dt))
            speed2--
        }else{
            if(speed2!=0f){
                car2.translateLocal(Vector3f(0.0f, 0.0f, speed2*dt))
            speed2++
            }
        }
        if(window.getKeyState(GLFW_KEY_DOWN)){
            car2.translateLocal(Vector3f(0.0f, 0.0f,50 * dt))
        }
        //println("Auto2"+car2.getPosition())
        println("Auto1"+car1.getPosition())
        //println(car2.getPosition().distance(car1.getPosition()))

        if (drehungFahrzeug1 == 0) {
            if (car1.getPosition().distance(-93.60f, 0.8016f, -47.47f) <= 1f) {
                println(drehungFahrzeug1)
                car1.rotateLocal(0f, Math.toRadians(-30f), 0f)
                drehungFahrzeug1 = 1
            }
            }else{
            if (drehungFahrzeug1 == 1) {
                if (car1.getPosition().distance(-68.08f, 0.8016f, -67.86f) <= 1f) {
                    println(drehungFahrzeug1)
                    car1.rotateLocal(0f, Math.toRadians(300f), 0f)
                    drehungFahrzeug1 = 2
                }
            }else{
                if (drehungFahrzeug1 == 2) {
                    if (car1.getPosition().distance(-47.01f, 0.8016f, -55.03f) <= 1f) {
                        println(drehungFahrzeug1)
                        car1.rotateLocal(0f, Math.toRadians(-30f), 0f)
                        drehungFahrzeug1 = 3
                    }
                }else{
                    if (drehungFahrzeug1 == 3) {
                        if (car1.getPosition().distance(4.126f, 0.8016f, 34.54f) <= 1f) {
                            println(drehungFahrzeug1)
                            car1.rotateLocal(0f, Math.toRadians(30f), 0f)
                            drehungFahrzeug1 = 4
                        }
                    }else{
                        if (drehungFahrzeug1 == 4) {
                            if (car1.getPosition().distance(7.545f, 0.8016f, 36.13f) <= 1f) {
                                println(drehungFahrzeug1)
                                car1.rotateLocal(0f, Math.toRadians(30f), 0f)
                                drehungFahrzeug1 = 5
                            }
                        }else{
                            if (drehungFahrzeug1 == 5) {
                                if (car1.getPosition().distance(16.97f, 0.8016f, 35.80f) <= 1f) {
                                    println(drehungFahrzeug1)
                                    car1.rotateLocal(0f, Math.toRadians(30f), 0f)
                                    drehungFahrzeug1 = 6
                                }
                            }else{
                                if (drehungFahrzeug1 == 6) {
                                    if (car1.getPosition().distance(26.40f, 0.8016f, 29.96f) <= 1f) {
                                        println(drehungFahrzeug1)
                                        car1.rotateLocal(0f, Math.toRadians(30f), 0f)
                                        drehungFahrzeug1 = 7
                                    }
                                }else{
                                    if (drehungFahrzeug1 == 7) {
                                        if (car1.getPosition().distance(56.87f, 0.8016f, -23.81f) <= 1f) {
                                            println(drehungFahrzeug1)
                                            car1.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                            drehungFahrzeug1 = 8
                                        }
                                    }else {
                                        if (drehungFahrzeug1 == 8) {
                                            if (car1.getPosition().distance(65.78f, 0.8016f, -28.56f) <= 1f) {
                                                println(drehungFahrzeug1)
                                                car1.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                drehungFahrzeug1 = 9
                                            }
                                        }else {
                                            if (drehungFahrzeug1 == 9) {
                                                if (car1.getPosition().distance(74.53f, 0.8016f, -28.23f) <= 1f) {
                                                    println(drehungFahrzeug1)
                                                    car1.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                    drehungFahrzeug1 = 10
                                                }
                                            }else {
                                                if (drehungFahrzeug1 == 10) {
                                                    if (car1.getPosition().distance(87.68f, 0.8016f, -20.06f) <= 1f) {
                                                        println(drehungFahrzeug1)
                                                        car1.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                        drehungFahrzeug1 = 11
                                                    }
                                                }else {
                                                    if (drehungFahrzeug1 == 11) {
                                                        if (car1.getPosition().distance(95.60f, 0.8016f, -5.675f) <= 1f) {
                                                            println(drehungFahrzeug1)
                                                            car1.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                            drehungFahrzeug1 = 12
                                                        }
                                                    }else {
                                                        if (drehungFahrzeug1 == 12) {
                                                            if (car1.getPosition().distance(95.27f, 0.8016f, 4.081f) <= 1f) {
                                                                println(drehungFahrzeug1)
                                                                car1.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                drehungFahrzeug1 = 13
                                                            }
                                                        }else {
                                                            if (drehungFahrzeug1 == 13) {
                                                                if (car1.getPosition().distance(65.27f, 0.8016f, 55.38f) <= 1f) {
                                                                    println(drehungFahrzeug1)
                                                                    car1.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                    drehungFahrzeug1 = 14
                                                                }
                                                            }else {
                                                                if (drehungFahrzeug1 == 14) {
                                                                    if (car1.getPosition().distance(53.48f, 0.8016f, 61.80f) <= 1f) {
                                                                        println(drehungFahrzeug1)
                                                                        car1.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                        drehungFahrzeug1 = 15
                                                                    }
                                                                }else {
                                                                    if (drehungFahrzeug1 == 15) {
                                                                        if (car1.getPosition().distance(-75.66f, 0.8016f, 61.30f) <= 1f) {
                                                                            println(drehungFahrzeug1)
                                                                            car1.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                            drehungFahrzeug1 = 16
                                                                        }
                                                                    }else {
                                                                        if (drehungFahrzeug1 == 16) {
                                                                            if (car1.getPosition().distance(-86.83f, 0.8016f, 54.46f) <= 1f) {
                                                                                println(drehungFahrzeug1)
                                                                                car1.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                                drehungFahrzeug1 = 17
                                                                            }
                                                                        }else {
                                                                            if (drehungFahrzeug1 == 17) {
                                                                                if (car1.getPosition().distance(-92.92f, 0.8016f, 43.25f) <= 1f) {
                                                                                    println(drehungFahrzeug1)
                                                                                    car1.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                                    drehungFahrzeug1 = 18
                                                                                }
                                                                            }else {
                                                                                if (drehungFahrzeug1 == 18) {
                                                                                    if (car1.getPosition().distance(-92.58f, 0.8016f, 29.83f) <= 1f) {
                                                                                        println(drehungFahrzeug1)
                                                                                        car1.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                                        drehungFahrzeug1 = 19
                                                                                    }
                                                                                }else {
                                                                                    if (drehungFahrzeug1 == 19) {
                                                                                        if (car1.getPosition().distance(-84.42f, 0.8016f, 16.68f) <= 1f) {
                                                                                            println(drehungFahrzeug1)
                                                                                            car1.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                                            drehungFahrzeug1 = 20
                                                                                        }
                                                                                    }else {
                                                                                        if (drehungFahrzeug1 == 20) {
                                                                                            if (car1.getPosition().distance(-77.24f, 0.8016f, 12.93f) <= 1f) {
                                                                                                println(drehungFahrzeug1)
                                                                                                car1.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                                                drehungFahrzeug1 = 21
                                                                                            }
                                                                                        }else {
                                                                                            if (drehungFahrzeug1 == 21) {
                                                                                                if (car1.getPosition().distance(-63.15f, 0.8016f, 13.26f) <= 1f) {
                                                                                                    println(drehungFahrzeug1)
                                                                                                    car1.rotateLocal(0f, Math.toRadians(30f), 0f)
                                                                                                    drehungFahrzeug1 = 22
                                                                                                }
                                                                                            }else {
                                                                                                if (drehungFahrzeug1 == 22) {
                                                                                                    if (car1.getPosition().distance(-53.14f, 0.8016f, 7.094f) <= 1f) {
                                                                                                        println(drehungFahrzeug1)
                                                                                                        car1.rotateLocal(0f, Math.toRadians(30f), 0f)
                                                                                                        drehungFahrzeug1 = 23
                                                                                                    }
                                                                                                }else {
                                                                                                    if (drehungFahrzeug1 == 23) {
                                                                                                        if (car1.getPosition().distance(-50.38f, 0.8016f, 1.654f) <= 1f) {
                                                                                                            println(drehungFahrzeug1)
                                                                                                            car1.rotateLocal(0f, Math.toRadians(30f), 0f)
                                                                                                            drehungFahrzeug1 = 24
                                                                                                            h1=1
                                                                                                        }
                                                                                                    }else {
                                                                                                        if (drehungFahrzeug1 == 24) {
                                                                                                            if (car1.getPosition().distance(-50.72f, 0.8016f, -2.102f) <= 1f) {
                                                                                                                println(drehungFahrzeug1)
                                                                                                                car1.rotateLocal(0f, Math.toRadians(30f), 0f)
                                                                                                                drehungFahrzeug1 = 25
                                                                                                            }
                                                                                                        }else {
                                                                                                            if (drehungFahrzeug1 == 25) {
                                                                                                                if (car1.getPosition().distance(-60.22f, 0.8016f, -17.89f) <= 1f) {
                                                                                                                    println(drehungFahrzeug1)
                                                                                                                    car1.rotateLocal(0f, Math.toRadians(30f), 0f)
                                                                                                                    drehungFahrzeug1 = 26
                                                                                                                }
                                                                                                            }else {
                                                                                                                if (drehungFahrzeug1 == 26) {
                                                                                                                    if (car1.getPosition().distance(-64.50f, 0.8016f, -19.98f) <= 1f) {
                                                                                                                        println(drehungFahrzeug1)
                                                                                                                        car1.rotateLocal(0f, Math.toRadians(30f), 0f)
                                                                                                                        drehungFahrzeug1 = 27
                                                                                                                    }
                                                                                                                }else {
                                                                                                                    if (drehungFahrzeug1 == 27) {
                                                                                                                        if (car1.getPosition().distance(-77.97f, 0.8016f, -19.48f) <= 1f) {
                                                                                                                            println(drehungFahrzeug1)
                                                                                                                            car1.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                                                                            drehungFahrzeug1 = 28
                                                                                                                        }
                                                                                                                    }else {
                                                                                                                        if (drehungFahrzeug1 == 28) {
                                                                                                                            if (car1.getPosition().distance(-88.81f, 0.8016f, -26.31f) <= 1f) {
                                                                                                                                println(drehungFahrzeug1)
                                                                                                                                car1.rotateLocal(0f, Math.toRadians(-60f), 0f)
                                                                                                                                drehungFahrzeug1 = 0
                                                                                                                                runde++
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (i == 1) {
            if (drehungFahrzeug2 == 0) {
                if (car2.getPosition().distance(-84f, 1.002f, -53f) <= 1f) {
                    println(drehungFahrzeug2)
                    car2.rotateLocal(0f, Math.toRadians(300f), 0f)
                    drehungFahrzeug2 = 1
                }
            } else {
                if (drehungFahrzeug2 == 1) {
                    if (car2.getPosition().distance(-69.57f, 1.002f, -60.36f) <= 1f) {
                        println(drehungFahrzeug2)
                        car2.rotateLocal(0f, Math.toRadians(300f), 0f)
                        drehungFahrzeug2 = 2
                    }
                } else {
                    if (drehungFahrzeug2 == 2) {
                        if (car2.getPosition().distance(-55.57f, 1.002f, -51.28f) <= 1f) {
                            println(drehungFahrzeug2)
                            car2.rotateLocal(0f, Math.toRadians(-30f), 0f)
                            drehungFahrzeug2 = 3
                        }
                    } else {
                        if (drehungFahrzeug2 == 3) {
                            if (car2.getPosition().distance(-7.848f, 1.002f, 32f) <= 1f) {
                                println(drehungFahrzeug2)
                                car2.rotateLocal(0f, Math.toRadians(30f), 0f)
                                drehungFahrzeug2 = 4
                            }
                        } else {
                            if (drehungFahrzeug2 == 4) {
                                if (car2.getPosition().distance(10.71f, 1.002f, 42.94f) <= 1f) {
                                    println(drehungFahrzeug2)
                                    car2.rotateLocal(0f, Math.toRadians(30f), 0f)
                                    drehungFahrzeug2 = 5
                                }
                            }else {
                                if (drehungFahrzeug2 == 5) {
                                    if (car2.getPosition().distance(18.52f, 1.002f, 42.11f) <= 1f) {
                                        println(drehungFahrzeug2)
                                        car2.rotateLocal(0f, Math.toRadians(30f), 0f)
                                        drehungFahrzeug2 = 6
                                    }
                                }else {
                                    if (drehungFahrzeug2 == 6) {
                                        if (car2.getPosition().distance(31.67f, 1.002f, 33.95f) <= 1f) {
                                            println(drehungFahrzeug2)
                                            car2.rotateLocal(0f, Math.toRadians(30f), 0f)
                                            drehungFahrzeug2 = 7
                                        }
                                    }else {
                                        if (drehungFahrzeug2 == 7) {
                                            if (car2.getPosition().distance(58.92f, 1.002f, -13.93f) <= 1f) {
                                                println(drehungFahrzeug2)
                                                car2.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                drehungFahrzeug2 = 8
                                            }
                                        }else {
                                            if (drehungFahrzeug2 == 8) {
                                                if (car2.getPosition().distance(68.24f, 1.002f, -18.73f) <= 1f) {
                                                    println(drehungFahrzeug2)
                                                    car2.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                    drehungFahrzeug2 = 9
                                                }
                                            }else {
                                                if (drehungFahrzeug2 == 9) {
                                                    if (car2.getPosition().distance(76.33f, 1.002f, -18.40f) <= 1f) {
                                                        println(drehungFahrzeug2)
                                                        car2.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                        drehungFahrzeug2 = 10
                                                    }
                                                }else {
                                                    if (drehungFahrzeug2 == 10) {
                                                        if (car2.getPosition().distance(83.45f, 1.002f, -13.90f) <= 1f) {
                                                            println(drehungFahrzeug2)
                                                            car2.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                            drehungFahrzeug2 = 11
                                                        }
                                                    }else {
                                                        if (drehungFahrzeug2 == 11) {
                                                            if (car2.getPosition().distance(88.09f, 1.002f, -4.871f) <= 1f) {
                                                                println(drehungFahrzeug2)
                                                                car2.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                drehungFahrzeug2 = 12
                                                            }
                                                        }else {
                                                            if (drehungFahrzeug2 == 12) {
                                                                if (car2.getPosition().distance(87.75f, 1.002f, -1.449f) <= 1f) {
                                                                    println(drehungFahrzeug2)
                                                                    car2.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                    drehungFahrzeug2 = 13
                                                                }
                                                            }else {
                                                                if (drehungFahrzeug2 == 13) {
                                                                    if (car2.getPosition().distance(54.26f, 1.002f, 55.57f) <= 1f) {
                                                                        println(drehungFahrzeug2)
                                                                        car2.rotateLocal(0f, Math.toRadians(-60f), 0f)
                                                                        drehungFahrzeug2 = 14
                                                                    }
                                                                }else {
                                                                    if (drehungFahrzeug2 == 14) {
                                                                        if (car2.getPosition().distance(-69.58f, 1.002f, 54.71f) <= 1f) {
                                                                            println(drehungFahrzeug2)
                                                                            car2.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                            drehungFahrzeug2 = 15
                                                                        }
                                                                    }else {
                                                                        if (drehungFahrzeug2 == 15) {
                                                                            if (car2.getPosition().distance(-82f, 1.002f, 47.04f) <= 1f) {
                                                                                println(drehungFahrzeug2)
                                                                                car2.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                                drehungFahrzeug2 = 16
                                                                            }
                                                                        }else {
                                                                            if (drehungFahrzeug2 == 16) {
                                                                                if (car2.getPosition().distance(-84.99f, 1.002f, 41.19f) <= 1f) {
                                                                                    println(drehungFahrzeug2)
                                                                                    car2.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                                    drehungFahrzeug2 = 17
                                                                                }
                                                                            }else {
                                                                                if (drehungFahrzeug2 == 17) {
                                                                                    if (car2.getPosition().distance(-84.66f, 1.002f, 31.77f) <= 1f) {
                                                                                        println(drehungFahrzeug2)
                                                                                        car2.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                                        drehungFahrzeug2 = 18
                                                                                    }
                                                                                }else {
                                                                                    if (drehungFahrzeug2 == 18) {
                                                                                        if (car2.getPosition().distance(-78.66f, 1.002f, 22.38f) <= 1f) {
                                                                                            println(drehungFahrzeug2)
                                                                                            car2.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                                            drehungFahrzeug2 = 19
                                                                                        }
                                                                                    }else {
                                                                                        if (drehungFahrzeug2 == 19) {
                                                                                            if (car2.getPosition().distance(-76.56f, 1.002f, 21.74f) <= 1f) {
                                                                                                println(drehungFahrzeug2)
                                                                                                car2.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                                                drehungFahrzeug2 = 20
                                                                                                h=1
                                                                                            }
                                                                                        }else {
                                                                                            if (drehungFahrzeug2 == 20) {
                                                                                                if (car2.getPosition().distance(-60.80f, 1.002f, 22.08f) <= 1f) {
                                                                                                    println(drehungFahrzeug2)
                                                                                                    car2.rotateLocal(0f, Math.toRadians(30f), 0f)
                                                                                                    drehungFahrzeug2 = 21
                                                                                                }
                                                                                            }else {
                                                                                                if (drehungFahrzeug2 == 21) {
                                                                                                    if (car2.getPosition().distance(-52.23f, 1.002f, 16.74f) <= 1f) {
                                                                                                        println(drehungFahrzeug2)
                                                                                                        car2.rotateLocal(0f, Math.toRadians(30f), 0f)
                                                                                                        drehungFahrzeug2 = 22
                                                                                                    }
                                                                                                }else {
                                                                                                    if (drehungFahrzeug2 == 22) {
                                                                                                        if (car2.getPosition().distance(-42.64f, 1.002f, -0.5334f) <= 1f) {
                                                                                                            println(drehungFahrzeug2)
                                                                                                            car2.rotateLocal(0f, Math.toRadians(30f), 0f)
                                                                                                            drehungFahrzeug2 = 23
                                                                                                        }
                                                                                                    }else {
                                                                                                        if (drehungFahrzeug2 == 23) {
                                                                                                            if (car2.getPosition().distance(-42.98f, 1.002f, -8.956f) <= 1f) {
                                                                                                                println(drehungFahrzeug2)
                                                                                                                car2.rotateLocal(0f, Math.toRadians(30f), 0f)
                                                                                                                drehungFahrzeug2 = 24
                                                                                                            }
                                                                                                        }else {
                                                                                                            if (drehungFahrzeug2 == 24) {
                                                                                                                if (car2.getPosition().distance(-51.81f, 1.002f, -23.26f) <= 1f) {
                                                                                                                    println(drehungFahrzeug2)
                                                                                                                    car2.rotateLocal(0f, Math.toRadians(30f), 0f)
                                                                                                                    drehungFahrzeug2 = 25
                                                                                                                }
                                                                                                            }else {
                                                                                                                if (drehungFahrzeug2 == 25) {
                                                                                                                    if (car2.getPosition().distance(-59.56f, 1.002f, -27.35f) <= 1f) {
                                                                                                                        println(drehungFahrzeug2)
                                                                                                                        car2.rotateLocal(0f, Math.toRadians(30f), 0f)
                                                                                                                        drehungFahrzeug2 = 26
                                                                                                                    }
                                                                                                                }else {
                                                                                                                    if (drehungFahrzeug2 == 26) {
                                                                                                                        if (car2.getPosition().distance(-76.32f, 1.002f, -27.01f) <= 1f) {
                                                                                                                            println(drehungFahrzeug2)
                                                                                                                            car2.rotateLocal(0f, Math.toRadians(-30f), 0f)
                                                                                                                            drehungFahrzeug2 = 27
                                                                                                                        }
                                                                                                                    }else {
                                                                                                                        if (drehungFahrzeug2 == 27) {
                                                                                                                            if (car2.getPosition().distance(-83.44f, 1.002f, -31.51f) <=1f) {
                                                                                                                                println(drehungFahrzeug2)
                                                                                                                                car2.rotateLocal(0f, Math.toRadians(-60f), 0f)
                                                                                                                                drehungFahrzeug2 = 0
                                                                                                                                runde2++
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //if(car1.getPosition().distance(car2.getPosition())<=1f){
            //println("Du hast verkackt")
        //}
       if(runde!=0) {
            if(h1!=0){
                if(car1.getPosition().distance(-93.60f, 0.8016f, -36.37f) <= 3f) {
                    aktuellePosition = car1.getPosition()
                    aktuellePosition.negate()
                    car1.translateLocal(aktuellePosition)
                    car1.translateLocal(Vector3f(-93.60f, 0.8016f, -36.37f))
                    println("Runde " + runde)
                    h1=0
                }
            }
       }
       if(runde2!=0) {
           if(h!=0){
                if(car2.getPosition().distance(-84.0f, 1.002f, -32.36f) <= 2f) {
                    aktuellePosition2 = car2.getPosition()
                    aktuellePosition2.negate()
                    car2.translateLocal(aktuellePosition2)
                    car2.translateLocal(Vector3f(-84.0f, 1.002f, -32.36f))
                    println("Runde " + runde)
                    h=0
                }
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
        if(i==0) {
            if (window.getKeyState(GLFW_KEY_P)) {
                car2.translateGlobal(Vector3f(0.0f, 2f, 0.0f))
                i = 1
            }
        }
        if(window.getKeyState(GLFW_KEY_R)){
            car1.setRotation(0f,0f,0f)
            aktuellePosition = car1.getPosition()
            aktuellePosition.negate()
            car1.translateLocal(aktuellePosition)
            car1.translateLocal(Vector3f(-93.60f, 0.8016f, -36.37f))
            drehungFahrzeug1 = 0
            speed = 0f
        }
        if(window.getKeyState(GLFW_KEY_L)){
            car2.setRotation(0f,0f,0f)
            aktuellePosition2 = car2.getPosition()
            aktuellePosition2.negate()
            car2.translateLocal(aktuellePosition2)
            car2.translateLocal(Vector3f(-84.0f, 1.002f, -32.36f))
            drehungFahrzeug2 = 0
            speed2 = 0f
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