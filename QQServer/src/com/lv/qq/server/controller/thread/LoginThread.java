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
			System.out.println("��ȡ����Ϣ��" + message.toString());
			Message reply = controller.dealWith(message);
			System.out.println("������Ϣ��" + reply.toString());
			if(message.getType() == 0){  //��Ӧ��¼
				oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(reply);
				Map<String, Object> result = (Map<String, Object>) reply.getContent();
				if((int)result.get("result") == 1){
					User user = (User) result.get("user");
					//����һ���µ�ͨ���߳���ͻ���ά��ͨ��
					CommunicationThread thread = new CommunicationThread(socket, controller, user.getAccount());
					ServerController.THREAD_MANAGER.put(user.getAccount(), thread);
					thread.start();
				}
			}else if(message.getType() == 9){  //��Ӧ�ļ��ϴ�
				//����һ���ļ�����ת���߳�ȥ������
				ChatWord chatWord = (ChatWord) reply.getContent();
				FileForwordThread fdfThread = new FileForwordThread(socket, chatWord);
				ServerController.UPLOAD_THREAD_MANAGER.put(chatWord.getId(), fdfThread);
			}else if(message.getType() == 11){  //��Ӧ�ļ�����
				String id = (String) reply.getContent();
				FileForwordThread fdfThread = ServerController.UPLOAD_THREAD_MANAGER.get(id);
				fdfThread.setAccepterSocket(socket);
				fdfThread.start();
			}else{  //��Ӧע�������ֱ�ӻ�ȡ������¼�
				oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(reply);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//�˴��ر�������ᵼ��socket�ر�
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
