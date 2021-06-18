package ar.edu.unahur.obj2.servidorWeb

interface Analizador{

    fun analizar(respuesta:Respuesta,modulo:Modulo)
}

class DeteccionDeDemoraEnRespuesta(val demoraMinima: Int) : Analizador {
    var respuestasDemoradas = 0


    override fun analizar(respuesta: Respuesta,modulo: Modulo) {
        if (respuesta.tiempo > demoraMinima){
            respuestasDemoradas += 1
        }
    }

    fun cantidadDeRespuestasDemoradas() = respuestasDemoradas
}

class IpsSospechosas(val listaDeIps : List<String>) : Analizador{

    override fun analizar(respuesta: Respuesta,modulo: Modulo){
        TODO()
    }

}

class Estadisticas() : Analizador{

    override fun analizar(respuesta: Respuesta, modulo: Modulo) {
        TODO("Not yet implemented")
    }
}

