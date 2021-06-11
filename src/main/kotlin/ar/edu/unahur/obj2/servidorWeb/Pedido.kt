package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime

class Pedido(val ip: String, val url: String, val fechaHora: LocalDateTime){

    fun verificarProtocolo() : Respuesta {
        return if (!url.startsWith("http"))
            Respuesta(CodigoHttp.NOT_IMPLEMENTED, "", 10, this)
        else
            Respuesta(CodigoHttp.OK, "hola", 12, this)

    }
}


