#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texture;
layout(location = 2) in vec3 normale;
//uniforms
// translation object to world
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

//uniform vec3 cyclePointLightPos;
uniform vec3 cycleSpotLightPos;
uniform vec2 tcMultiplier;          //anlegen von Uniform -> in den Shader laden

uniform vec3 eckeLightPos;
out struct VertexData
{
    vec3 position;
    vec2 texture;
    vec3 normale;
    //vec3 toPointLight;
    vec3 toSpotLight;
    vec3 toEckLicht;
} vertexData;



void main(){
    mat4 modelView = view_matrix * model_matrix;
    vec4 pos = modelView * vec4(position, 1.0f);
    //vec4 norm = model_matrix * vec4(normale, 1.0f);
    vec4 norm = inverse(transpose(modelView)) * vec4(normale, 1.0f);

    //vec4 lp = view_matrix * vec4(cyclePointLightPos, 1.0f);     //position im Viewspace
    //vertexData.toPointLight = (lp - pos).xyz;                   //Richtungsvektor

    vec4 lp2 = view_matrix * vec4(cycleSpotLightPos, 1.0f);
    vertexData.toSpotLight = (lp2 - pos).xyz;

    vec4 lp3 = view_matrix * vec4(eckeLightPos, 1.0f);
    vertexData.toEckLicht = (lp3 - pos).xyz;
    //gl_Position = vec4(pos.xy, -pos.z, 1.0f);
    gl_Position = projection_matrix * view_matrix * model_matrix * vec4(position, 1.0f);
    vertexData.position = -pos.xyz;
    vertexData.texture = texture * tcMultiplier;                                                                        //wiederholende Textur, sonst wird die Textur nur einmal in groß angezeigt
    vertexData.normale = norm.xyz;
}