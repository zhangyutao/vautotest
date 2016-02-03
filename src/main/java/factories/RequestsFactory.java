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
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationMemberValue;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import requests.CommandRequest;
import requests.E2EValidationRequest;
import requests.EmailRequest;
import requests.RestfRequest;
import requests.ScenarioRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
	private final static String PREFIX = "insteadOf$";
	private final static String CONNECT = "$";

	/**
	 * initiate the class
	 * 
	 * @param client
	 * @param requestsList
	 * @return
	 */
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
		return (T) list;

	}

	/**
	 * initiate the class which needs to be updated with provided annotation
	 * 
	 * @param client
	 * @param requestsList
	 * @return
	 */
	public static <T> T initElementsOfUpdatedRequestList(Client client, Class<T> requestsList,
			HashMap<Field, Annotation[]> newAnnotaionsMap) {
		Class<?> newList;
		T newObj = null;
		try {
			newList = getNewRequestListFromUpdate(requestsList, newAnnotaionsMap);
			newObj = initElements(client, requestsList, newList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return newObj;
	}

	private static <T> T initElements(Client client, Class<T> oldrequestsList, Class<?> newRequestsList) {
		Object list = null;

		try {
			Constructor<?> constructor = newRequestsList.getConstructor();
			list = constructor.newInstance();
		} catch (NoSuchMethodException e) {
			try {
				list = newRequestsList.newInstance();
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
		boolean isAutoGenList = false;
		for (Field field : fields) {
			if (field.getName().contains(PREFIX)) {
				isAutoGenList = true;
				break;
			}
		}
		if (isAutoGenList) {

			T oldList = null;
			try {
				Constructor<T> constructor = oldrequestsList.getConstructor();
				oldList = constructor.newInstance();
			} catch (NoSuchMethodException e) {
				try {
					oldList = oldrequestsList.newInstance();

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

			Field[] oldFields = oldList.getClass().getDeclaredFields();
			for (Field oldField : oldFields) {
				boolean findnew = false;
				for (Field newfield : fields) {
					if (newfield.getName().equals(oldField.getName())) {
						findnew = true;
						Object value = decorate(client, newfield);
						if (value != null) {
							try {
								oldField.setAccessible(true);
								oldField.set(oldList, value);
							} catch (IllegalAccessException e) {
								throw new RuntimeException(e);
							}
						}
						break;
					}
				}
				if (!findnew) {
					Object value = decorate(client, oldField);
					if (value != null) {
						try {
							oldField.setAccessible(true);
							oldField.set(oldList, value);
						} catch (IllegalAccessException e) {
							throw new RuntimeException(e);
						}
					}
				}

			}
			return oldList;
		} else {
			throw new RuntimeException("the new list is not valid");
		}

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
				if (properties.scenario() != null & properties.inputDatas() != null & properties.iteration() > 0) {
					ScenarioRequest thiscase = new ScenarioRequest();
					thiscase.setScenarioClient((ScenarioClient) commandClient);

					try {
						Constructor<? extends Scenario> constructor1 = properties.scenario().getConstructor();
						thiscase.setScenario(constructor1.newInstance());

						ScenarioIO[] inputs = new ScenarioIO[properties.inputDatas().length];
						int index = 0;
						for (Class<? extends ScenarioIO> inputData : properties.inputDatas()) {
							Constructor<? extends ScenarioIO> constructor2 = inputData.getConstructor();
							inputs[index] = constructor2.newInstance();
							index = index + 1;
						}
						thiscase.setInputDatas(inputs);
					} catch (NoSuchMethodException e) {
						try {
							thiscase.setScenario(properties.scenario().newInstance());
							ScenarioIO[] inputs = new ScenarioIO[properties.inputDatas().length];
							int index = 0;
							for (Class<? extends ScenarioIO> inputData : properties.inputDatas()) {
								inputs[index] = inputData.newInstance();
								index = index + 1;
							}
							thiscase.setInputDatas(inputs);
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

	/**
	 * get the all annotations of the field which you provided
	 * 
	 * @param field
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Annotation[] getAnnotations(Field field) {
		ClassPool pool = ClassPool.getDefault();
		CtClass ctClass;
		AnnotationsAttribute attr = null;
		try {
			ctClass = pool.getCtClass(field.getDeclaringClass().getName());

			CtField ctField = ctClass.getField(field.getName());

			attr = (AnnotationsAttribute) ctField.getFieldInfo().getAttribute(AnnotationsAttribute.visibleTag);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		if (attr == null) {
			return null;
		} else {
			return attr.getAnnotations();
		}
	}

	/**
	 * make a HashMap about AnnotationContent which can be used in
	 * {@link factories.RequestsFactory#getNewAnnotationFromUpdate(Field,
	 * Annotation, HashMap<String, MemberValue>)}
	 * 
	 * @param memberNames
	 * @param memberValues
	 * @return
	 * @throws Exception
	 */
	private static <T> HashMap<String, MemberValue> makeAnnotationContent(Field field, String[] memberNames,
			Object[] memberValues) {
		CtClass ctClass;
		CtField oldField;
		CtField newField;
		ConstPool newFieldConstPool = null;
		try {
			ctClass = ClassPool.getDefault().getCtClass(field.getDeclaringClass().getName());
			oldField = ctClass.getField(field.getName());
			newField = new CtField(oldField.getType(), field.getName(), ctClass);
			newFieldConstPool = newField.getFieldInfo().getConstPool();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		HashMap<String, MemberValue> content = new HashMap<String, MemberValue>();
		if (memberNames.length != memberValues.length) {
			throw new IllegalArgumentException("Length of memberNames and memberValues shoul be same.");
		}
		for (int i = 0; i < memberNames.length; i++) {
			MemberValue value = null;
			if (memberValues[i] instanceof String) {
				value = (MemberValue) new StringMemberValue((String) memberValues[i], newFieldConstPool);
			}

			if (memberValues[i] instanceof Class) {
				value = (MemberValue) new ClassMemberValue(((Class<?>) memberValues[i]).getName(), newFieldConstPool);
			}
			if (memberValues[i] instanceof MemberValue) {
				value = (MemberValue) memberValues[i];
			}
			content.put(memberNames[i], value);
		}
		return content;
	}

	/**
	 * make a HashMap about AnnotationMap which can be used in
	 * {@link factories.RequestsFactory#getNewRequestListFromUpdate(Class<?> ,
	 * HashMap<Field, Annotation[]>)
	 * 
	 * @param fields
	 * @param annotationArraies
	 * @return
	 */
	@SuppressWarnings("unused")
	private static HashMap<Field, Annotation[]> makeAnnotationMap(Field[] fields, Annotation[][] annotationArraies) {
		HashMap<Field, Annotation[]> content = new HashMap<Field, Annotation[]>();
		if (fields.length != annotationArraies.length) {
			throw new IllegalArgumentException("Length of fields and annotationArraies shoul be same.");
		}
		for (int i = 0; i < fields.length; i++) {
			content.put(fields[i], annotationArraies[i]);
		}
		return content;
	}

	/**
	 * change a value of a annotation of a "request" class and return a new map
	 * 
	 * @param oldMap
	 * @param fieldName
	 * @param annotationName
	 * @param annotationMemberName
	 *            - the name of members which you want to edit
	 * @param annotationMemberValue
	 *            - the value of members which you want to update, and the index
	 *            of it must match to the index of parameter
	 *            annotationMemberName}.
	 * @return
	 */
	public static <T> HashMap<Field, Annotation[]> updateAnnotationMap(HashMap<Field, Annotation[]> oldMap,
			String fieldName, String annotationName, String[] annotationMemberName, Object[] annotationMemberValue) {
		if (annotationMemberName.length != annotationMemberValue.length) {
			throw new IllegalArgumentException(
					"size of annotationMemberName and annotationMemberValue should be same!");
		}
		HashMap<Field, Annotation[]> newMap = new HashMap<Field, Annotation[]>();
		Iterator<Entry<Field, Annotation[]>> iter = oldMap.entrySet().iterator();
		boolean findField = false;
		while (iter.hasNext()) {
			Map.Entry<Field, Annotation[]> entry = iter.next();
			Field oldField = entry.getKey();
			Annotation[] oldAnnotas = entry.getValue();
			if (oldField.getName().equals(fieldName)) {
				Annotation[] newAnnotas = new Annotation[oldAnnotas.length];
				findField = true;
				boolean findAnno = false;
				int i = 0;
				for (Annotation a : oldAnnotas) {
					if (a.getTypeName().endsWith("." + annotationName)) {
						findAnno = true;
						Set<?> memberNames = a.getMemberNames();
						boolean findMember = false;
						String[] annotationMemberNames = new String[memberNames.size()];
						Object[] annotationMemberValues = new Object[memberNames.size()];
						int j = 0;
						for (Object m : memberNames) {
							boolean findExpected = false;
							int v = 0;
							for (String expectedname : annotationMemberName) {
								if (m.toString().equals(expectedname)) {
									findExpected = true;
									annotationMemberNames[j] = expectedname;
									annotationMemberValues[j] = annotationMemberValue[v];
									break;
								}
								v = v + 1;
							}
							if (findExpected) {
								findMember = true;
							} else {
								annotationMemberNames[j] = m.toString();
								annotationMemberValues[j] = (Object) a.getMemberValue(m.toString());
							}
							j = j + 1;
						}
						HashMap<String, MemberValue> newAnnotationContent = makeAnnotationContent(oldField,
								annotationMemberNames, annotationMemberValues);
						Annotation newAnno = getNewAnnotationFromUpdate(oldField, a, newAnnotationContent);
						newAnnotas[i] = newAnno;
						if (!findMember) {
							throw new IllegalArgumentException("annotationMemberName \"" + annotationMemberName
									+ "\" is not found in that annotation: " + annotationName);
						}

					} else {
						newAnnotas[i] = a;
					}
					i = i + 1;
				}
				newMap.put(oldField, newAnnotas);
				if (!findAnno) {
					throw new IllegalArgumentException(
							"annotationName \"" + annotationName + "\" is not found in that field: " + fieldName);
				}

			} else {
				newMap.put(oldField, oldAnnotas);
			}
		}
		if (!findField) {
			throw new IllegalArgumentException("fieldName \"" + fieldName + "\" is not found in that Class");
		}
		return newMap;
	}

	/**
	 * change a value of a annotation of a "request" class and return a new map
	 * 
	 * @param oldMap
	 * @param fieldName
	 * @param annotationName
	 * @param annotationIndex
	 *            - if there are same annotation name write on the field, please
	 *            provide the index of occurrence of the annotation.
	 * @param annotationMemberName
	 *            - the name of members which you want to edit
	 * @param annotationMemberValue
	 *            - the value of members which you want to update, and the index
	 *            of it must match to the index of parameter
	 *            annotationMemberName}.
	 * @return
	 */
	public static <T> HashMap<Field, Annotation[]> updateAnnotationMap(HashMap<Field, Annotation[]> oldMap,
			String fieldName, String annotationName, int annotationIndex, String[] annotationMemberName,
			Object[] annotationMemberValue) {
		HashMap<Field, Annotation[]> newMap = new HashMap<Field, Annotation[]>();

		if (annotationMemberName.length != annotationMemberValue.length) {
			throw new IllegalArgumentException(
					"size of annotationMemberName and annotationMemberValue should be same!");
		}

		if (annotationName.endsWith("Header") || annotationName.endsWith("Cookie") || annotationName.endsWith("Message")
				|| annotationName.endsWith("Line")) {
			Iterator<Entry<Field, Annotation[]>> iter = oldMap.entrySet().iterator();
			boolean findField = false;
			while (iter.hasNext()) {
				Map.Entry<Field, Annotation[]> entry = iter.next();
				Field oldField = entry.getKey();
				Annotation[] oldAnnotas = entry.getValue();
				if (oldField.getName().equals(fieldName)) {
					CtField newField;
					ConstPool newFieldConstPool = null;
					CtClass ctFieldType;
					try {
						ctFieldType = ClassPool.getDefault().getCtClass(oldField.getType().getName());
						newField = new CtField(ctFieldType, oldField.getName(),
								ClassPool.getDefault().getCtClass(oldField.getDeclaringClass().getName()));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw new RuntimeException(e);
					}
					newFieldConstPool = newField.getFieldInfo().getConstPool();
					Annotation[] newAnnotas = new Annotation[oldAnnotas.length];
					findField = true;
					boolean findAnno = false;
					int i = 0;
					for (Annotation a : oldAnnotas) {
						if ((a.getTypeName().endsWith(".HeaderContainer")
								|| a.getTypeName().endsWith(".CookieContainer")
								|| a.getTypeName().endsWith(".MessageContainer")
								|| a.getTypeName().endsWith(".LineContainer")) & (findAnno == false)) {

							Set<?> allnames = a.getMemberNames();
							Annotation newannot = new Annotation(a.getTypeName(), newFieldConstPool);
							for (Object each : allnames) {
								ArrayMemberValue arramv = (ArrayMemberValue) a.getMemberValue(each.toString());
								MemberValue[] amvs = arramv.getValue();
								AnnotationMemberValue[] newamvs = new AnnotationMemberValue[amvs.length];
								int o = 0;
								for (MemberValue amv : amvs) {
									Annotation oldsubAnno = ((AnnotationMemberValue) amv).getValue();
									if (oldsubAnno.getTypeName().endsWith(annotationName) & o == annotationIndex) {
										findAnno = true;

										Set<?> names = oldsubAnno.getMemberNames();
										boolean findMember = false;
										String[] annotationMemberNames = new String[names.size()];
										Object[] annotationMemberValues = new Object[names.size()];
										int j = 0;
										for (Object n : names) {

											boolean findExpected = false;
											int v = 0;
											for (String expectedname : annotationMemberName) {
												if (n.toString().equals(expectedname)) {
													findExpected = true;
													annotationMemberNames[j] = expectedname;
													annotationMemberValues[j] = annotationMemberValue[v];
													break;
												}
												v = v + 1;
											}

											if (findExpected) {
												findMember = true;
											} else {
												annotationMemberNames[j] = n.toString();
												annotationMemberValues[j] = (Object) oldsubAnno
														.getMemberValue(n.toString());
											}
											j = j + 1;

										}
										if (!findMember) {
											throw new IllegalArgumentException(
													"annotationMemberName \"" + annotationMemberName
															+ "\" is not found in that annotation: " + annotationName);
										}
										HashMap<String, MemberValue> newAnnotationContent = makeAnnotationContent(
												oldField, annotationMemberNames, annotationMemberValues);
										Annotation newAnno = getNewAnnotationFromUpdate(oldField, oldsubAnno,
												newAnnotationContent);

										AnnotationMemberValue newamv = new AnnotationMemberValue(newAnno,
												newFieldConstPool);
										newamvs[o] = newamv;
									} else {
										newamvs[o] = (AnnotationMemberValue) amv;
									}

									o = o + 1;
								}
								ArrayMemberValue newarramv = new ArrayMemberValue(newFieldConstPool);
								newarramv.setValue(newamvs);
								newannot.addMemberValue(each.toString(), newarramv);
								break;
							}
							newAnnotas[i] = newannot;
						} else {
							newAnnotas[i] = a;
						}
						i = i + 1;
					}
					newMap.put(oldField, newAnnotas);
					if (!findAnno) {
						throw new IllegalArgumentException(
								"annotationName \"" + annotationName + "\" is not found in that field: " + fieldName);
					}
				} else {
					newMap.put(oldField, oldAnnotas);
				}
			}
			if (!findField) {
				throw new IllegalArgumentException("fieldName \"" + fieldName + "\" is not found in that Class");
			}
			return newMap;
		} else {
			newMap = updateAnnotationMap(oldMap, fieldName, annotationName, annotationMemberName,
					annotationMemberValue);
			return newMap;
		}

	}

	/**
	 * get a new annotation which is created according to the update on the
	 * original annotation of the field you provided
	 * 
	 * @param field
	 * @param originalAnnotation
	 * @param content
	 *            - HashMap<String, MemberValue> is <String MemberName,
	 *            MemberValue value>
	 * @return
	 */
	private static <T> Annotation getNewAnnotationFromUpdate(Field field, Annotation originalAnnotation,
			HashMap<String, MemberValue> annotationContent) {
		CtClass ctFieldType = null;
		CtField ctField = null;
		Annotation newanno = null;
		try {
			ctFieldType = ClassPool.getDefault().getCtClass(field.getType().getName());
			ctField = new CtField(ctFieldType, field.getName(),
					ClassPool.getDefault().getCtClass(field.getDeclaringClass().getName()));
			newanno = new Annotation(originalAnnotation.getTypeName(), ctField.getFieldInfo().getConstPool());

			Iterator<Entry<String, MemberValue>> iter = annotationContent.entrySet().iterator();
			while (iter.hasNext()) {

				Map.Entry<String, MemberValue> entry = iter.next();
				newanno.addMemberValue(entry.getKey(), entry.getValue());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return newanno;
	}

	/**
	 * get all annotaions of all fields of the class you provided
	 * 
	 * @param requestClass
	 * @return
	 */
	public static HashMap<Field, Annotation[]> getAnnotationsMap(Class<?> requestClass) {
		HashMap<Field, Annotation[]> allinfo = new HashMap<Field, Annotation[]>();
		try {
			ClassPool pool = ClassPool.getDefault();
			CtClass ctClass;

			ctClass = pool.getCtClass(requestClass.getName());

			CtField[] fields = ctClass.getFields();
			for (int i = 0; i < fields.length; i++) {
				FieldInfo fieldInfo = fields[i].getFieldInfo();
				AnnotationsAttribute attr = (AnnotationsAttribute) fieldInfo
						.getAttribute(AnnotationsAttribute.visibleTag);
				Annotation[] annotations = attr.getAnnotations();
				allinfo.put(requestClass.getField(fields[i].getName()), annotations);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return allinfo;
	}

	/**
	 * return a new class which annotation was updated base on you provided
	 * 
	 * @param requestClass
	 * @param newAnnotaionsMap
	 *            - the new annotation you want to use in the new class
	 * @return
	 * @throws Exception
	 */
	private static Class<?> getNewRequestListFromUpdate(Class<?> requestClass,
			HashMap<Field, Annotation[]> newAnnotaionsMap) throws Exception {

		ClassPool pool = ClassPool.getDefault();
		int seq = 1;
		String newClassName = requestClass.getName() + String.valueOf(seq);
		while (pool.find(newClassName) != null) {
			seq = seq + 1;
			newClassName = requestClass.getName() + String.valueOf(seq);
		}
		CtClass ctClass = pool.makeClass(newClassName);
		Iterator<Entry<Field, Annotation[]>> iter = newAnnotaionsMap.entrySet().iterator();
		// ctClass.setSuperclass(pool.getCtClass(requestClass.getName()));
		String name = requestClass.getName().replace(".", CONNECT);
		CtField flagField = new CtField(pool.getCtClass("java.lang.String"), PREFIX + name, ctClass);
		flagField.setModifiers(Modifier.PRIVATE);
		ctClass.addField(flagField);
		while (iter.hasNext()) {

			Map.Entry<Field, Annotation[]> entry = iter.next();
			Field field = entry.getKey();
			Annotation[] annotations = entry.getValue();
			CtField oldField = pool.getCtClass(requestClass.getName()).getField(field.getName());
			CtField newField = new CtField(oldField.getType(), field.getName(), ctClass);
			// newField.setGenericSignature(oldField.getSignature());
			newField.setModifiers(Modifier.PUBLIC);
			ConstPool newFieldConstPool = newField.getFieldInfo().getConstPool();
			AnnotationsAttribute attr = new AnnotationsAttribute(newFieldConstPool, AnnotationsAttribute.visibleTag);

			for (Annotation anno : annotations) {
				Annotation newannot = new Annotation(anno.getTypeName(), newFieldConstPool);

				if (anno.getTypeName().endsWith(".HeaderContainer") || anno.getTypeName().endsWith(".CookieContainer")
						|| anno.getTypeName().endsWith(".MessageContainer")
						|| anno.getTypeName().endsWith(".LineContainer")) {
					Set<?> allnames = anno.getMemberNames();
					for (Object each : allnames) {
						ArrayMemberValue arramv = (ArrayMemberValue) anno.getMemberValue(each.toString());
						MemberValue[] amvs = arramv.getValue();
						AnnotationMemberValue[] newamvs = new AnnotationMemberValue[amvs.length];
						int i = 0;
						for (MemberValue amv : amvs) {
							Annotation oldsubAnno = ((AnnotationMemberValue) amv).getValue();
							Annotation newsubAnno = new Annotation(oldsubAnno.getTypeName(), newFieldConstPool);
							Set<?> names = oldsubAnno.getMemberNames();
							for (Object n : names) {
								MemberValue mv = oldsubAnno.getMemberValue(n.toString());
								newsubAnno.addMemberValue(n.toString(), mv);
							}
							AnnotationMemberValue newamv = new AnnotationMemberValue(newsubAnno, newFieldConstPool);
							newamvs[i] = newamv;
							i = i + 1;
						}
						ArrayMemberValue newarramv = new ArrayMemberValue(newFieldConstPool);
						newarramv.setValue(newamvs);
						newannot.addMemberValue(each.toString(), newarramv);
						break;
					}
				} else {
					Set<?> allnames = anno.getMemberNames();
					for (Object each : allnames) {
						MemberValue mv = anno.getMemberValue(each.toString());
						newannot.addMemberValue(each.toString(), mv);
					}
				}

				attr.addAnnotation(newannot);
			}

			newField.setAttribute(attr.getName(), attr.get());
			ctClass.addField(newField);
		}

		return ctClass.toClass();

	}

}
