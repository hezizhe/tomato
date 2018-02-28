package com.lv.qq.server.controller.thread;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

import com.lv.qq.common.vo.ChatWord;
import com.lv.qq.common.vo.Message;
import com.lv.qq.common.vo.User;
import com.lv.qq.server.controller.ServerController;

public class LoginThread extends Thread {

	private ServerController controller;
	private Socket socket;
	
	public LoginThread(Socket socket, ServerController controller) {
		this.controller = controller;
		this.socket = socket;
	}
	
	@SuppressWarnings("unchecked")
	public void run(){
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			Message message = (Message) ois.readObject();
			System.out.println("读取到信息：" + message.toString());
			Message reply = controller.dealWith(message);
			System.out.println("返回信息：" + reply.toString());
			if(message.getType() == 0){  //响应登录
				oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(reply);
				Map<String, Object> result = (Map<String, Object>) reply.getContent();
				if((int)result.get("result") == 1){
					User user = (User) result.get("user");
					//创建一个新的通信线程与客户端维持通信
					CommunicationThread thread = new CommunicationThread(socket, controller, user.getAccount());
					ServerController.THREAD_MANAGER.put(user.getAccount(), thread);
					thread.start();
				}
			}else if(message.getType() == 9){  //响应文件上传
				//创建一个文件接收转发线程去处理它
				ChatWord chatWord = (ChatWord) reply.getContent();
				FileForwordThread fdfThread = new FileForwordThread(socket, chatWord);
				ServerController.UPLOAD_THREAD_MANAGER.put(chatWord.getId(), fdfThread);
			}else if(message.getType() == 11){  //响应文件下载
				String id = (String) reply.getContent();
				FileForwordThread fdfThread = ServerController.UPLOAD_THREAD_MANAGER.get(id);
				fdfThread.setAccepterSocket(socket);
				fdfThread.start();
			}else{  //响应注册等其他直接获取结果的事件
				oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(reply);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//此处关闭输出流会导致socket关闭
		/*finally {
			try {
				if(oos != null) oos.close();
				if(ois != null) oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
	}

}
