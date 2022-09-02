package org.alliedoffsets.service

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import spray.json.DefaultJsonProtocol._
import org.slf4j.LoggerFactory

final case class TestProjectData(id: Long, name: String, registry: String)

trait ProjectDataService {
  
  implicit val system = ActorSystem(Behaviors.empty, "AdminToolsServiceApp")
  implicit val projectMarshaller: spray.json.RootJsonFormat[TestProjectData] = jsonFormat3(TestProjectData.apply)

    val log = LoggerFactory.getLogger("org.alliedoffsets.service.ProjectsService")

    def runService(host: String, port: Int): Unit = {
      log.info(s"Starting service on host: ${host} with port ${port}")
      val route = get {
        path("project" / LongNumber) {
          projectId => complete(TestProjectData(projectId, "TestProject", "VCS"))
        }
      }

      Http().newServerAt(host, port).bind(route)
    }
}
