> ǰ����Ҫ����һ�¸߼��������(Java)�Ŀ��裬ֻ��һ�ܲ�����ʱ��
�����Ѿ������Խ������ģʽ������д�����߰������������
��ƪ��������ʦҪ���ύ��˳�㴫�����ͺ�README��Ȩ����¼һ��

# һ�����������
## 1.���˼·
��������һ��֧���������죬�������ԣ������ļ������ͱ�������������ң��������ͻ��˷��룬�ͻ��˸�����Ϣͨ��ͼ�λ�����չʾ���û����������û�ִ�з�����Ϣ������˽�ģ��������䣬�ϴ��ļ��������ļ��ȹ��ܣ�ͬʱ�е������������ݵ�ְ�𣬿ͻ���ͨ��SSLSocket���ʷ���ˣ���������Ϣ����������ˣ��ٽ������˵���Ϣ�ӷ������ȡ���Ѵﵽ��������Ĺ��ܡ�
������Ϊ���˸�����һѧ�ڵĸ߼�������ƿγ̵�ѧ����֪ʶ��һЩ���ⲹ���֪ʶ��������Ҫ�õ���Java�������SQL��Swing��֪ʶ��������ʵ�ж����������������֧����ʮ�ּ򵥵ķ�ʽ�������⣬����һ����ʵ���ԡ�
## 2.�������
Ϊ�˸��õ���Ƴ��������ȷ����˳�������Ҫ�Ĺ��ܣ�������Ĺ��ܷ�Ϊ�������֣����죬�ļ����û�����������ͼ��ʾ��
![c741fa96446a95fc8e72d5483d412551.png](https://img.gejiba.com/images/c741fa96446a95fc8e72d5483d412551.png)
## 3.��������
### 3.1 ����Ĺ���
#### 3.1.1 ������
������Ŀͻ��˲�����Ҫ����������ɣ��ֱ�Ϊ��Files��Chat��Net��UI��Utils��
���У�Files������������ļ������Ĳ��֣�Chat��Ϊ�����ʹ�õĵ�һЩ���ݽṹ����File����������һ���ļ�����ʵ���ƣ����������ƣ��ϴ�ʱ�䣬����Ⱥ�ģ�Message��������һ�������¼���͵��û��������͵�ʱ�䣬���͵�ID�ȣ�Net��Ϊ�����罻���Ĳ��֣������˶�������������ļ������������ӣ���һЩ����ĸ�ʽ��UI��Ϊ�ͻ��˵�ǰ�˲��֣�������һЩUI��form�ļ��Ͷ�Ӧ�Ĺ������ڵ��ࡣUtils��ΪһЩȫ�ֱ����;�̬�����Ĵ�Ŵ���
ͬʱ�����Ļ���Ҳͬ��������Ϊ����Ĺ���ģ�黮�֣�������ĸ������ֶ���������
������ķ������ͻ��˽ṹ���ƣ����ڲ���Ҫ��ʾͼ���������ȥ����UI�����������ֽṹ����һ�£�����Ͳ���׸���ˡ�
#### 3.1.2 ����˼��
�ڳ������֮�����Ҿ�����ʶ�س��Խ�����ֲ㣬�Ӹ�Ϊ�ײ���ļ������ݿ����ֿ�ʼ��������Ӧ�ó���ʹ�ϲ������������²��ĳЩ���ܣ����ĳ����ſɷ�Ϊ���㣺��ʾ�㡢�߼��㡢���ݲ㡣��ʾ�㸺����Ϣ���ݸ��û������ݲ㸺���ȡ��д�����ݣ��߼���λ����ʾ������ݲ�֮�䣬��������ݽ��д��������ݸ��û������û���ָʾ������������ݲ㡣<br />
![b1991b7e8293ba83e15870a80e022355.png](https://img.gejiba.com/images/b1991b7e8293ba83e15870a80e022355.png)

#### 3.1.3 ��Ĺ���
���潫�԰�Ϊ��λ�ֱ���ܳ���ĸ����ࡣ

##### (1)Chat��
Chat����Ϊ�򵥣���Ҫ�ɼ����໥����������ɣ�File��Message��PrivateMessage��Room��User���ֱ�����������Ӧ�Ķ���

##### (2)Files��
Files����ֻ��һ���ࡪ��FileStream���������Ҫ������û���Ϣ��������Ϣ��˽����Ϣ�����л��ͷ����л����������ļ���

##### (3)UI��
UI���а���������ǰ��ͼ��������form��ÿ�����о�������ͼ�񴰿ڵ�һЩ���ã������壬����ȣ�ͬʱҲ����һЩ���ڴ����¼��ļ��������Է�����Ϣ���������û�������Ͱ�ť���ᴥ����Ӧ��ť�ļ��������������н��ᴴ���µ�SendMessage����ͨ��ChatSeverConnection��������������

##### (4) Net��
Net��������������Ϊ���ӵİ�����Ҫ��Ϊ������֣�Request���е��ඨ�������е�������ʽ��Feedback���ж��������з�����������Ϣ����ʽ��FileServerConnection�ฺ�����ļ������������ӣ�ChatServerConnection�ฺ������������������ӣ�Handler�ദ�����ܵ���������������
![9d7e3b1bd73e82b6bc926e12ddd212dc.jpg](https://img.gejiba.com/images/9d7e3b1bd73e82b6bc926e12ddd212dc.jpg)
ChatServerConnection�������ļ���ʱ������һ������������������ӣ�ÿ���յ���Ϣ������һ���µ��߳̽��յ�����Ϣ���͸�Handler�����������ǵ��ļ������������ܲ�û����ôƵ��������FileServerConnection��û��ʵ�ֳ־û����ӣ����ҵ���Ҫ����ʱ������һ���µ����ӡ�
Request����Feedback���о�������ʮ������ͷ��ص����ͣ���Feedback��Ϊ�����ڲ������˸������͵�����������Ҫ�������Ϣ�������ü̳й�ϵ��ߴ�����ظ������ʣ�
![b3d6b12391b3ee2848242b1e09a5106a.png](https://img.gejiba.com/images/b3d6b12391b3ee2848242b1e09a5106a.png)
Handler�����Ȼ���ܵ�����ChatServerConnection���յ���Object�࣬��������ת��ΪFeedback�࣬��ȡ���е�type������ȷ����������ݰ����Ͳ�ת���ɶ�Ӧ���࣬Ȼ���ٽ��ж�Ӧ�Ĵ�����
##### (5)Utils��
Utils���а����ĸ����֣�EmojiParsers, StaticBuffer, StaticConfig, Utils��
EmojiParsers�ฺ��������Ϣ�ж�Ӧ�ı������ת��ΪHTMLͼƬ��ʽ���Ա���ǰ����ʾ��Ӧ��ȷ�ı��顣
StaticBuffer������Ҫ����һЩ����Ҫ�������ȫ�ֱ������統ǰ���ߵ��û������֣��ļ��б��ȡ�
StaticConfig������Ҫ����һЩ��Ҫ�������ȫ�ֱ����������з���������¼�����˵������¼�ȡ�
Utils���а���һЩ��̬��������Ҫ������ߴ���ĸ����ʣ���ʱ���ʽת���ȡ�
### 3.2 ��������
#### 3.2.1 ��������
![3a99c08a8cc28d003ca07cc0f69beca1.png](https://img.gejiba.com/images/3a99c08a8cc28d003ca07cc0f69beca1.png)
### 3.3 ���н�ͼ
#### 3.3.1 ��¼����
![4de82a2a0583ce74f17dd9f239631a59.png](https://img.gejiba.com/images/4de82a2a0583ce74f17dd9f239631a59.png)
#### 3.3.2 ע�����
![b94dbd58e6628d29a1e341050d9d1516.png](https://img.gejiba.com/images/b94dbd58e6628d29a1e341050d9d1516.png)
#### 3.3.3 ������
![8f0aff2384f93cea49d1abca876b1991.png](https://img.gejiba.com/images/8f0aff2384f93cea49d1abca876b1991.png)
#### 3.3.4 �ļ��б�
![75217c9a447b56846561749eab8a0720.png](https://img.gejiba.com/images/75217c9a447b56846561749eab8a0720.png)
#### 3.3.5 ˽�Ľ���
![63eaa10c32890a8281ae4840071c12c7.png](https://img.gejiba.com/images/63eaa10c32890a8281ae4840071c12c7.png)
#### 3.3.6 �ϴ��ɹ�
![8f4695a7e59cca6fb077f63488809a6e.png](https://img.gejiba.com/images/8f4695a7e59cca6fb077f63488809a6e.png)
#### 3.3.7 ������ʾ
![4f1253e6ee1f3bf4a8e985176bc4d549.png](https://img.gejiba.com/images/4f1253e6ee1f3bf4a8e985176bc4d549.png)
## 4 ϵͳʵ��
### 4.1 ϵͳ�ؼ�����
�ͻ���ǰ����Ҫ����Swingͼ�λ�����ʵ�֣���˵��������Ӳ���SSLSocketʵ�֡�����˲���MYSQL���SSLSocketʵ�֡�
#### 4.1.1 Swing
##### (1)�������ʾ
������֧����Էḻ�������Զ���ı��飬��Ҫͨ��Swing��Textpane�ؼ�ʵ�֣�
![4f1253e6ee1f3bf4a8e985176bc4d549.png](https://img.gejiba.com/images/4f1253e6ee1f3bf4a8e985176bc4d549.png)
Textpane֧��չʾHTML��ʽ���ļ����������ֻ��Ҫ�ȶ���һ���ؼ����ַ������� /jiaran/ Ȼ�������ֱ������Textpane֮ǰʹ��HTML��<img>��ǩ�����滻����Ӧ�Ĺؼ��ַ������Ϳ��ԴﵽչʾͼƬ�����Ч����
�滻ǰ���ַ������滻����ַ�������EmojiParsers�ж��壬ֻ��Ҫͨ���޸�������ַ�������Ϳ��Ա�ݵ����Ӹ����Զ���ı��顣

##### (2)�����¼��ˢ��
����������˶�ʱ��ѯ�ķ�ʽȥ��ȡ��������������Ϣ���趨Ϊÿһ���������ѯ��һ���Ƿ����µ�Ⱥ�������¼����˽�������¼��Ȼ�����뱾�ص������¼�Աȣ���������������ǰ�ˣ�����д�������¼��
��������Ҫʹ����TimerTask����ʵ�ֶ�ʱִ��ĳ���̵߳Ĺ��ܡ�

#### 4.1.2 �������ݵĳ־û�
��������Ҫ������Java��ݵ����л��ͷ����л����ܣ����ڱ��������еĹؼ������ʵ����Serializable�ӿڣ�����ֻ��Ҫ�򵥵ļ��оͿ���ʵ�����л��ͷ����л����ܡ��ļ��Ķ�дҲ���Բ���FileOutputStream������ʵ�֡�

#### 4.1.3 ��������
���ǵ����Ĵ����û�������һ�ַǳ��������ʵ�ַ����������������е����紫���о�Ӧ����SSLSocket��˫������Ա�֤�û���Ϣ�İ�ȫ�ɿ����������ͨ��Socket���ӣ���������Ҫ����˺Ϳͻ��˶��жԷ��ɽ��ܵ�֤�飬���򽫲������ӡ�

#### 4.1.4 ����洢
����������û�������κ�SHA���ܵķ�ʽ�洢���룬��¼ʱ���Աȼ��ܺ��ֵ���������������û��������ģ��Ա�֤�û����밲ȫ��

#### 4.1.5 �ļ��ϴ�������
�������ļ�����Ķ˿����������Ķ˿�����룬������е��ļ��������Ҫʹ�ö���Ķ˿ڣ�ͬʱ����Ҳ��SSL���ܵġ�<br />��������Ҫ�õ���DataOutputStream��OutputStream����DataOutputstream���д�����������ͣ��ϴ�/���أ����ļ�����Ϣ���ļ������ļ���С��������OutputStream�����ļ���������ǰ�Ѿ��������ļ��Ĵ�С��Ϣ�����Գ������ͨ���Ѵ�����ļ���С��ʵ���ļ��Ĵ�С�Ա����ж��ļ��Ƿ�����ϣ��ڿͻ��ˣ�����ͨ��JProgressBar�ؼ����ϴ�/���ؽ��ȷ������û���

## 5 ϵͳʵʩ�Ͳ���
### 5.1 ��������
Java 17
MYSQL8.0
������֧�����������汾��δ��ʵ�ʲ��ԡ�
### 5.2 ����Ļ�ȡ
�ͻ���Github��ַ: [https://github.com/WiDayn/QuickChat](https://github.com/WiDayn/QuickChat)
�����Github��ַ: [https://github.com/WiDayn/QuickChatServer](https://github.com/WiDayn/QuickChatServer)
��������Դ������ֱ������Releases�й����õ�jar�����С�

### 5.3 ����Ĳ���
1. �������srcĿ¼�µ�Dump_QuickChat.sql���ݿ�ṹ����mysql���ݿ��У��������ݿ�����ΪQuickChat��
2. �ڷ���˺Ϳͻ���Ŀ¼�¾�����Files�ļ������ڱ����ļ���
3. ͨ��JDK�Դ���keytools.exe����˫����ܵĿͻ���֤��������֤�飬��ֱ��ʹ�ó����Ŀ¼�Ѿ����ɺõ�֤�����ڲ��ԡ�
4. �������������������ͻ��ˣ���������ִ�У���һ��ʹ����Ҫ����ע�ᡣ���е��û���ʼʱ��������IDΪ1�Ĵ���Ⱥ�ġ�
