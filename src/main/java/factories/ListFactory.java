package factories;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import annotations.checkpoint.Elements;
import annotations.cmd.Line;
import annotations.cmd.containers.LineContainer;
import annotations.email.Message;
import annotations.email.containers.MessageContainer;
import annotations.httprequest.Auth;
import annotations.httprequest.Body;
import annotations.httprequest.Cookie;
import annotations.httprequest.Header;
import annotations.httprequest.Method;
import annotations.httprequest.URL;
import annotations.httprequest.UseSSL;
import annotations.httprequest.containers.CookieContainer;
import annotations.httprequest.containers.HeaderContainer;
import basic.Client;
import basic.Factory;
import basic.ServerCheckpointElements;
import elements.Command;
import elements.Email;
import elements.RestfRequest;
import elements.ServerCheckpoint;
import utilities.E2EValidationClient;
import utilities.EmailClient;
import utilities.PSClient;
import utilities.RestfClient;
import utilities.SQLClient;
import utilities.SSHClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListFactory implements Factory {
	public static <T> T initElements(Client commandClient, Class<T> CommandLineListClass) {
		T list = null;

		try {
			try {
				Constructor<T> constructor = CommandLineListClass.getConstructor();
				list = constructor.newInstance();
			} catch (NoSuchMethodException e) {
				list = CommandLineListClass.newInstance();
			}
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}

		Field[] fields = list.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object value = decorate(commandClient, field);
			if (value != null) {
				try {
					field.setAccessible(true);
					field.set(list, value);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return list;
	}

	private static Object decorate(Client commandClient, Field field) {

		if (!(Command.class.isAssignableFrom(field.getType()))
				&& !(RestfRequest.class.isAssignableFrom(field.getType()))
				&& !(Email.class.isAssignableFrom(field.getType()))
				&& !(ServerCheckpoint.class.isAssignableFrom(field.getType()))) {
			return null;
		}

		if (Command.class.isAssignableFrom(field.getType()) && ((commandClient instanceof PSClient)
				|| (commandClient instanceof SSHClient) || (commandClient instanceof SQLClient))) {
			if (field.getAnnotation(Line.class) == null && field.getAnnotation(LineContainer.class) == null) {
				return null;
			} else {

				if (field.getAnnotation(LineContainer.class) == null) {
					Line line = (Line) field.getAnnotation(Line.class);
					String text = line.value();
					Command newcmd = new Command();
					newcmd.setLine(text);
					newcmd.setClient(commandClient);
					return newcmd;
				} else {
					Line[] lines = null;
					LineContainer lc = (LineContainer) field.getAnnotation(LineContainer.class);
					lines = lc.value();
					if (lines.length == 1) {
						String text = lines[0].value();
						Command newcmd = new Command();
						newcmd.setLine(text);
						newcmd.setClient(commandClient);
						return newcmd;
					} else {
						String[] templines = new String[lines.length];
						for (int i = 0; i < lines.length; i++) {
							templines[i] = lines[i].value();
							;
						}
						Command newcmd = new Command();
						newcmd.setLines(templines);
						newcmd.setClient(commandClient);
						return newcmd;
					}
				}

			}

		} else if (RestfRequest.class.isAssignableFrom(field.getType()) && (commandClient instanceof RestfClient)) {
			if (field.getAnnotation(Method.class) == null || field.getAnnotation(URL.class) == null) {
				return null;
			} else {
				Method methodAnnotation = (Method) field.getAnnotation(Method.class);
				String method = methodAnnotation.value().toUpperCase();

				URL urlAnnotation = (URL) field.getAnnotation(URL.class);
				String URL = urlAnnotation.value();

				String body = "";
				if (field.getAnnotation(Body.class) != null) {
					Body bodyAnnotation = (Body) field.getAnnotation(Body.class);
					body = bodyAnnotation.value();
				}

				boolean isSsl = false;
				if (field.getAnnotation(UseSSL.class) != null) {
					UseSSL useSSLAnnotation = (UseSSL) field.getAnnotation(UseSSL.class);
					isSsl = useSSLAnnotation.value();
				}

				RestfRequest rf = new RestfRequest();
				rf.setClient((RestfClient) commandClient);

				Header[] headers = null;
				Map<String, String> requestHeaders = new HashMap<String, String>();
				if (field.getAnnotation(HeaderContainer.class) != null) {
					HeaderContainer hc = (HeaderContainer) field.getAnnotation(HeaderContainer.class);
					headers = hc.value();
					for (int i = 0; i < headers.length; i++) {
						requestHeaders.put(headers[i].key(), headers[i].value());
					}
				} else if (field.getAnnotation(Header.class) != null) {
					Header header = (Header) field.getAnnotation(Header.class);
					requestHeaders.put(header.key(), header.value());
				}

				Cookie[] cookies = null;
				Map<String, String> requestCookies = new HashMap<String, String>();
				if (field.getAnnotation(CookieContainer.class) != null) {
					CookieContainer cc = (CookieContainer) field.getAnnotation(CookieContainer.class);
					cookies = cc.value();
					for (int i = 0; i < cookies.length; i++) {
						requestCookies.put(cookies[i].key(), cookies[i].value());
					}
				} else if (field.getAnnotation(Cookie.class) != null) {
					Cookie cookie = (Cookie) field.getAnnotation(Cookie.class);
					requestCookies.put(cookie.key(), cookie.value());
				}

				if (field.getAnnotation(Auth.class) != null) {
					Auth auth = (Auth) field.getAnnotation(Auth.class);
					Map<String, String> authcontent = new HashMap<String, String>();
					authcontent.put("type", auth.type());
					authcontent.put("realm", auth.realm());
					authcontent.put("password", auth.password());
					if (auth.preemptive()) {
						authcontent.put("preemptive", "true");
					} else {
						authcontent.put("preemptive", "false");
					}
					authcontent.put("username", auth.username());

					rf.setRequest(method, URL, requestHeaders, requestCookies, body, isSsl, authcontent);
					return rf;
				} else {
					rf.setRequest(method, URL, requestHeaders, requestCookies, body, isSsl);
					return rf;
				}

			}

		} else if (Email.class.isAssignableFrom(field.getType()) && (commandClient instanceof EmailClient)) {

			Email email = new Email();
			email.setClient((EmailClient) commandClient);
			if (field.getAnnotation(MessageContainer.class) != null) {
				Message[] messages = null;
				MessageContainer mc = (MessageContainer) field.getAnnotation(MessageContainer.class);
				messages = mc.value();
				javax.mail.Message[] msgs = new javax.mail.Message[messages.length];
				for (int b = 0; b < messages.length; b++) {
					Message message = messages[b];
					if (!message.from().equals("") & !message.to().equals("") & !message.subject().equals("")) {
						try {
							msgs[b] = email.getClient().setupMessage(message.from(), message.to(), message.subject(),
									message.textBody(), message.attachmentsPath());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return null;
						}

					} else {
						return null;
					}
				}
				email.setupMessages(msgs);
				return email;
			} else if (field.getAnnotation(Message.class) != null) {
				Message message = (Message) field.getAnnotation(Message.class);
				if (!message.from().equals("") & !message.to().equals("") & !message.subject().equals("")) {
					try {
						email.setupMessage(message.from(), message.to(), message.subject(), message.textBody(),
								message.attachmentsPath());
						return email;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}

				} else {
					return null;
				}
			} else {
				return null;
			}
		} else if (ServerCheckpoint.class.isAssignableFrom(field.getType())
				&& (commandClient instanceof E2EValidationClient)) {
			if (field.getAnnotation(Elements.class) != null) {
				E2EValidationClient validationClient = new E2EValidationClient();
				ServerCheckpoint serverCheckpoint = new ServerCheckpoint();
				Elements elements = (Elements) field.getAnnotation(Elements.class);
				if (elements.expObj() != null & elements.actObj() != null & elements.comparison() != null) {
					ServerCheckpointElements arg = new ServerCheckpointElements();
					ArrayList<Object> eles = new ArrayList<Object>();
					try {
						eles.add(elements.expObj().newInstance());
						eles.add(elements.actObj().newInstance());
						eles.add(elements.comparison().newInstance());
					} catch (InstantiationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						return null;
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						return null;
					}
					try {
						arg.setElements(eles);
						serverCheckpoint.setElements(arg);
						serverCheckpoint.setClient(validationClient);
						return serverCheckpoint;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}

				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
