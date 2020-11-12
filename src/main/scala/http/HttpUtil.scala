package http

import java.io.File

import akka.http.scaladsl.server.directives.FileInfo

trait HttpUtil {

  protected def tempDestination(fileInfo: FileInfo): File =
    File.createTempFile(fileInfo.fileName, ".tmp")
}
