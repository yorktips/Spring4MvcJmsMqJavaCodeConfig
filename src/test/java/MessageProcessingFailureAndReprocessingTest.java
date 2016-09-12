//import java.util.Arrays;
//import java.util.Date;
//
//import javax.jms.ConnectionFactory;
//
//import org.apache.activemq.broker.BrokerService;
//import org.junit.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.context.annotation.*;
//import org.springframework.jms.listener.DefaultMessageListenerContainer;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.test.context.ContextConfiguration; 
//import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
//
//
//@ContextConfiguration(locations="com.prototypo.queue.MessageProcessingFailureAndReprocessingTest$ContextConfig",
//loader=JavaConfigContextLoader.class)
//public class MessageProcessingFailureAndReprocessingTest  extends AbstractJUnit4SpringContextTests {
//@Autowired
//private FailureReprocessTestScenario testScenario;
//
//@Before
//public void setUp() {
//testScenario.start();
//}
//
//@After
//public void tearDown() throws Exception {
//testScenario.stop();
//}
//
//@Test public void 
//should_reprocess_task_after_processing_failure() {
//try {
//    Thread.sleep(20*1000);
//
//    assertThat(testScenario.succeedingWorker.processedTasks, is(Arrays.asList(new String[]{
//            "task-1",
//    })));
//} catch (InterruptedException e) {
//    fail();
//}
//}
//
//@Configurable
//public static class FailureReprocessTestScenario {
//@Autowired
//public BrokerService broker;
//
//@Autowired
//public MockTaskProducer mockTaskProducer;
//
//@Autowired
//public FailingWorker failingWorker;
//
//@Autowired
//public SucceedingWorker succeedingWorker;
//
//@Autowired
//public TaskScheduler scheduler;
//
//public void start() {
//    Date now = new Date();
//    scheduler.schedule(new Runnable() {
//        public void run() { failingWorker.start(); }
//    }, now);
//
//    Date after1Seconds = new Date(now.getTime() + 1*1000);
//    scheduler.schedule(new Runnable() {
//        public void run() { mockTaskProducer.produceTask(); }
//    }, after1Seconds);
//
//    Date after2Seconds = new Date(now.getTime() + 2*1000);
//    scheduler.schedule(new Runnable() {
//        public void run() {
//            failingWorker.stop();
//            succeedingWorker.start();
//        }
//    }, after2Seconds);
//}
//
//public void stop() throws Exception {
//    succeedingWorker.stop();
//    broker.stop();
//}
//}
//
//@Configuration
//@ImportResource(value={"classpath:applicationContext-jms.xml",
//    "classpath:applicationContext-task.xml"})
//public static class ContextConfig {
//@Autowired
//private ConnectionFactory jmsFactory;
//
//@Bean
//public FailureReprocessTestScenario testScenario() {
//    return new FailureReprocessTestScenario();
//}
//
//@Bean
//public MockTaskProducer mockTaskProducer() {
//    return new MockTaskProducer();
//}
//
//@Bean
//public FailingWorker failingWorker() {
//    TaskListener listener = new TaskListener();
//    FailingWorker worker = new FailingWorker(listenerContainer(listener));
//    listener.setProcessor(worker);
//    return worker;
//}
//
//@Bean
//public SucceedingWorker succeedingWorker() {
//    TaskListener listener = new TaskListener();
//    SucceedingWorker worker = new SucceedingWorker(listenerContainer(listener));
//    listener.setProcessor(worker);
//    return worker;
//}
//
//private DefaultMessageListenerContainer listenerContainer(TaskListener listener) {
//    DefaultMessageListenerContainer listenerContainer = new DefaultMessageListenerContainer();
//    listenerContainer.setConnectionFactory(jmsFactory);
//    listenerContainer.setDestinationName("tasksQueue");
//    listenerContainer.setMessageListener(listener);
//    listenerContainer.setAutoStartup(false);
//    listenerContainer.initialize();
//    return listenerContainer;
//}
//
//}
//
//public static class FailingWorker implements TaskProcessor {
//private Logger LOG = Logger.getLogger(FailingWorker.class.getName());
//
//private final DefaultMessageListenerContainer listenerContainer;
//
//public FailingWorker(DefaultMessageListenerContainer listenerContainer) {
//    this.listenerContainer = listenerContainer;
//}
//
//public void start() {
//    LOG.info("FailingWorker.start()");
//    listenerContainer.start();
//}
//
//public void stop() {
//    LOG.info("FailingWorker.stop()");
//    listenerContainer.stop();
//}
//
//@Override
//public void processTask(Object task) {
//    LOG.info("FailingWorker.processTask(" + task + ")");
//    try {
//        Thread.sleep(1*1000);
//        throw Throwables.propagate(new Exception("Simulate task processing failure"));
//    } catch (InterruptedException e) {
//        LOG.log(Level.SEVERE, "Unexpected interruption exception");
//    }
//}
//}
//
//public static class SucceedingWorker implements TaskProcessor {
//private Logger LOG = Logger.getLogger(SucceedingWorker.class.getName());
//
//private final DefaultMessageListenerContainer listenerContainer;
//
//public final List<String> processedTasks;
//
//public SucceedingWorker(DefaultMessageListenerContainer listenerContainer) {
//    this.listenerContainer = listenerContainer;
//    this.processedTasks = new ArrayList<String>();
//}
//
//public void start() {
//    LOG.info("SucceedingWorker.start()");
//    listenerContainer.start();
//}
//
//public void stop() {
//    LOG.info("SucceedingWorker.stop()");
//    listenerContainer.stop();
//}
//
//@Override
//public void processTask(Object task) {
//    LOG.info("SucceedingWorker.processTask(" + task + ")");
//    try {
//        TextMessage taskText = (TextMessage) task;
//        processedTasks.add(taskText.getText());
//    } catch (JMSException e) {
//        LOG.log(Level.SEVERE, "Unexpected exception during task processing");
//    }
//}
//}
//
//}