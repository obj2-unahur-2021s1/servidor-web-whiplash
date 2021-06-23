package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime

interface Analizador{

    fun analizar(respuesta:Respuesta,modulo:Modulo)

}

class DeteccionDeDemoraEnRespuesta(val demoraMinima: Int) : Analizador {


    override fun analizar(respuesta: Respuesta,modulo: Modulo) {
        if (respuesta.tiempo > demoraMinima){
            modulo.respuestasDemoradas.add(respuesta)
        }
    }

    fun cantidadDeRespuestasDemoradas(modulo : Modulo) = modulo.respuestasDemoradas.size
}

class IpsSospechosas(val listaDeIps: List<String>) : Analizador{
    val listaDeIpsSospechosasAnalizadas = mutableListOf<Respuesta>()
    val modulosConsultadosPorIpSospechosas = mutableListOf<Modulo>()

    override fun analizar(respuesta: Respuesta,modulo: Modulo){
        if (listaDeIps.contains(respuesta.pedido.ip)) {
            listaDeIpsSospechosasAnalizadas.add(respuesta)
            modulo.sumarConsultaSospechosa()
            modulosConsultadosPorIpSospechosas.add(modulo)
        }
    }

    fun cuantosPedidosRealizo(ip: String) =
        listaDeIpsSospechosasAnalizadas.count{it.pedido.ip == ip}


    fun moduloMasConsultadoPorIpsSospechosas() : Modulo{
        return modulosConsultadosPorIpSospechosas.maxByOrNull{it.cantConsultasSospechosas}!!
    }

    fun conjuntoDeIpsSospechosas(ruta : String) : Set<String>{
        val listaDePedidos = listaDeIpsSospechosasAnalizadas.map{it.pedido}
        return listaDePedidos.filter{it.ruta() == ruta}.map{it.ip}.toSet()
    }

}

class Estadisticas() : Analizador{
    val listaDeRespuestas = mutableListOf<Respuesta>()

    override fun analizar(respuesta: Respuesta, modulo: Modulo) {
        listaDeRespuestas.add(respuesta)
    }

    fun tiempoDeRespuestaPromedio() =
        listaDeRespuestas.sumBy{it.tiempo} / listaDeRespuestas.size


    fun cantidadDePedidosEntreDosMomentos(fecha1 : LocalDateTime, fecha2:LocalDateTime):Int {
       return listaDeRespuestas.count { it.pedido.fechaHora in fecha1..fecha2 }
    }

    fun respuestasConBodyTipo(stringDeterminado : String) : Int {

        return listaDeRespuestas.count{it.body.contains(stringDeterminado) }
    }

}

