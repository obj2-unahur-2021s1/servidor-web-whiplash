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

    fun cantidadDeRespuestasDemoradas(modulo:Modulo) : Int {
        TODO("implemtar???")
    }
}

