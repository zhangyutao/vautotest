package factories;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import annotations.containers.cmd.LineContainer;
import annotations.containers.email.MessageContainer;
import annotations.containers.restf.CookieContainer;
import annotations.containers.restf.HeaderContainer;
import annotations.request.cmd.Line;
import annotations.request.e2evalidation.Items;
import annotations.request.email.Message;
import annotations.request.restf.Auth;
import annotations.request.restf.Body;
import annotations.request.restf.Cookie;
import annotations.request.restf.Header;
import annotations.request.restf.Method;
import annotations.request.restf.URL;
import annotations.request.restf.UseSSL;
import annotations.request.scenario.Properties;
import basic.Client;
import basic.Comparison;
import basic.E2ECheckpointElements;
import basic.Factory;
import basic.e2evalidation.EndpointObject;
import basic.scenario.Scenario;
import basic.scenario.ScenarioIO;
import clients.E2EValidationClient;
import clients.EmailClient;
import clients.PSClient;
import clients.RestfClient;
import clients.SQLClient;
import clients.SSHClient;
import clients.ScenarioClient;
import requests.CommandRequest;
import requests.E2EValidationRequest;
import requests.EmailRequest;
import requests.RestfRequest;
import requests.ScenarioRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * the class is used to initiate a class which contains class type:
 * {@link requests.CommandRequest},{@link requests.E2EValidationRequest},
 * {@link requests.EmailRequest},{@link requests.RestfRequest},
 * {@link requests.ScenarioRequest}
 * 
 * @author zhangyutao
 *
 */
public class RequestsFactory implements Factory {
	public static <T> T initElements(Client client, Class<T> requestsList) {
		T list = null;

		try {
			Constructor<T> constructor = requestsList.getConstructor();
			list = constructor.newInstance();
		} catch (NoSuchMethodException e) {
			try {
				list = requestsList.newInstance();
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				throw new RuntimeException(e1);
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				throw new RuntimeException(e1);
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Field[] fields = list.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object value = decorate(client, field);
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

		if (!(CommandRequest.class.isAssignableFrom(field.getType()))
				&& !(RestfRequest.class.isAssignableFrom(field.getType()))
				&& !(EmailRequest.class.isAssignableFrom(field.getType()))
				&& !(E2EValidationRequest.class.isAssignableFrom(field.getType()))
				&& !(ScenarioRequest.class.isAssignableFrom(field.getType()))) {
			return null;
		}

		if (CommandRequest.class.isAssignableFrom(field.getType()) && ((commandClient instanceof PSClient)
				|| (commandClient instanceof SSHClient) || (commandClient instanceof SQLClient))) {
			if (field.getAnnotation(Line.class) == null && field.getAnnotation(LineContainer.class) == null) {
				return null;
			} else {

				if (field.getAnnotation(LineContainer.class) == null) {
					Line line = (Line) field.getAnnotation(Line.class);
					String text = line.value();
					CommandRequest newcmd = new CommandRequest();
					newcmd.setLine(text);
					newcmd.setClient(commandClient);
					return newcmd;
				} else {
					Line[] lines = null;
					LineContainer lc = (LineContainer) field.getAnnotation(LineContainer.class);
					lines = lc.value();
					if (lines.length == 1) {
						String text = lines[0].value();
						CommandRequest newcmd = new CommandRequest();
						newcmd.setLine(text);
						newcmd.setClient(commandClient);
						return newcmd;
					} else {
						String[] templines = new String[lines.length];
						for (int i = 0; i < lines.length; i++) {
							templines[i] = lines[i].value();
							;
						}
						CommandRequest newcmd = new CommandRequest();
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

		} else if (EmailRequest.class.isAssignableFrom(field.getType()) && (commandClient instanceof EmailClient)) {

			EmailRequest email = new EmailRequest();
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
		} else if (E2EValidationRequest.class.isAssignableFrom(field.getType())
				&& (commandClient instanceof E2EValidationClient)) {
			if (field.getAnnotation(Items.class) != null) {
				E2EValidationRequest serverCheckpoint = new E2EValidationRequest();
				Items elements = (Items) field.getAnnotation(Items.class);
				if (elements.expObj() != null & elements.actObj() != null & elements.comparison() != null) {
					E2ECheckpointElements arg = new E2ECheckpointElements();
					ArrayList<Object> eles = new ArrayList<Object>();
					try {

						try {
							Constructor<? extends EndpointObject> constructor1 = elements.expObj().getConstructor();
							Constructor<? extends EndpointObject> constructor2 = elements.actObj().getConstructor();
							Constructor<? extends Comparison> constructor3 = elements.comparison().getConstructor();
							eles.add(constructor1.newInstance());
							eles.add(constructor2.newInstance());
							eles.add(constructor3.newInstance());
						} catch (NoSuchMethodException e) {
							eles.add(elements.expObj().newInstance());
							eles.add(elements.actObj().newInstance());
							eles.add(elements.comparison().newInstance());
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw new RuntimeException(e);
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw new RuntimeException(e);
						}

					} catch (InstantiationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						throw new RuntimeException(e1);

					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						throw new RuntimeException(e1);
					}
					try {
						arg.setElements(eles);
						serverCheckpoint.setElements(arg);
						serverCheckpoint.setClient(commandClient);
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
		} else if (ScenarioRequest.class.isAssignableFrom(field.getType())
				&& (commandClient instanceof ScenarioClient)) {
			if (field.getAnnotation(Properties.class) != null) {
				Properties properties = (Properties) field.getAnnotation(Properties.class);
				if (properties.scenario() != null & properties.inputdata() != null & properties.iteration() > 0) {
					ScenarioRequest thiscase = new ScenarioRequest();
					thiscase.setScenarioClient((ScenarioClient) commandClient);
					try {
						thiscase.setScenario(properties.scenario().newInstance());
						thiscase.setDatainput(properties.inputdata().newInstance());
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}
					try {
						Constructor<? extends Scenario> constructor1 = properties.scenario().getConstructor();
						Constructor<? extends ScenarioIO> constructor2 = properties.inputdata().getConstructor();

						thiscase.setScenario(constructor1.newInstance());
						thiscase.setDatainput(constructor2.newInstance());
					} catch (NoSuchMethodException e) {
						try {
							thiscase.setScenario(properties.scenario().newInstance());
							thiscase.setDatainput(properties.inputdata().newInstance());
						} catch (InstantiationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							throw new RuntimeException(e);
						} catch (IllegalAccessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							throw new RuntimeException(e);
						}

					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw new RuntimeException(e);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw new RuntimeException(e);
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw new RuntimeException(e);
					}

					thiscase.setIteration(properties.iteration());
					thiscase.setConcurrent(properties.isConcurrent());
					thiscase.setTimeout(properties.timeout());
					return thiscase;

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
