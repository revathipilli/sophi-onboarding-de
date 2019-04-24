package v1.post
import java.util.UUID

import javax.inject.{Inject, Singleton}
import akka.actor.ActorSystem
import com.sun.jmx.snmp.Timestamp
import play.api.libs.concurrent.CustomExecutionContext
import play.api.{Logger, MarkerContext}

import scala.collection.immutable.TreeSet
import scala.concurrent.Future

//final case class PostData(id: PostId, ArticleName:String,

//title: String, body: String)


casSerialVersionUID(0L)
final case class FileDescriptorProto(
                                      DateSubmitted:Timestamp,
                                      ArticleID:UUID,
                                      PopularityIndex:Double,AuthorsName: String,
                                      ReadershipIndex:Double,
                                      TotalEngagementTime:Timestamp) extends scalapb.GeneratedMessage with scalapb.Message[FileDescriptorProto] with scalapb.lenses.Updatable[FileDescriptorProto] {
  @transient
  private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
  private[this] def __computeSerializedValue(): _root_.scala.Int = {
    var __size = 0
    if (name.isDefined) {
      val __value = name.get
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(1, __value)
    };
    if (`package`.isDefined) {
      val __value = `package`.get
      __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
    };

  }
  final override def serializedSize: _root_.scala.Int = {
    var read = __serializedSizeCachedValue
    if (read == 0) {
      read = __computeSerializedValue()
      __serializedSizeCachedValue = read
    }
    reade class ArticleId(id: String =  UUID.randomUUID().toString) extends ModelEntityKey

object ArticleID{
  implicit def fromUUID(uuid: UUID): ArticleId = {
    ArticleId(id = uuid.toString)
  }
}

class PostId private (val underlying: Int) extends AnyVal {
  override def toString: String = underlying.toString
}

object PostId {
  def apply(raw: String): PostId = {
    require(raw != null)
    new PostId(Integer.parseInt(raw))
  }
}

class PostExecutionContext @Inject()(actorSystem: ActorSystem)
    extends CustomExecutionContext(actorSystem, "repository.dispatcher")

/**
  * A pure non-blocking interface for the PostRepository.
  */
trait PostRepository {
  def create(data: PostData)(implicit mc: MarkerContext): Future[PostId]

  def list()(implicit mc: MarkerContext): Future[Iterable[PostData]]

  def get(id: PostId)(implicit mc: MarkerContext): Future[Option[PostData]]
}

/**
  * A trivial implementation for the Post Repository.
  *
  * A custom execution context is used here to establish that blocking operations should be
  * executed in a different thread than Play's ExecutionContext, which is used for CPU bound tasks
  * such as rendering.
  */
@Singleton
class PostRepositoryImpl @Inject()()(implicit ec: PostExecutionContext)
    extends PostRepository {

  private val logger = Logger(this.getClass)

  private val postList = List(
    PostData(PostId("1"), "", "blog post 1"),
    PostData(PostId("2"), "title 2", "blog post 2"),
    PostData(PostId("3"), "title 3", "blog post 3"),
    PostData(PostId("4"), "title 4", "blog post 4"),
    PostData(PostId("5"), "title 5", "blog post 5")
  )

  override def list()(
      implicit mc: MarkerContext): Future[Iterable[PostData]] = {
    Future {
      logger.trace(s"list: ")
      postList
    }
  }

  override def get(id: PostId)(
      implicit mc: MarkerContext): Future[Option[PostData]] = {
    Future {
      logger.trace(s"get: id = $id")
      postList.find(post => post.id == id)
    }
  }

  def create(data: PostData)(implicit mc: MarkerContext): Future[PostId] = {
    Future {
      logger.trace(s"create: data = $data")
      data.id
    }
  }

}
