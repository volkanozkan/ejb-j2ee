package client;

import model.Users;

import java.util.Hashtable;
import javax.naming.NamingException;

import javax.naming.Context;
import javax.naming.InitialContext;
import bean.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MainClassClient {

	private static final String STATEFUL_ADRESS = "/fifth/StatefulBean!bean.UsersBeanRemote";
	private static final String STATELESS_ADRESS = "/fifth/StatelessBean!bean.UsersBeanRemote";

	public static void main(String[] args) {

		Context context = null;

		MainClassClient rc = new MainClassClient();

		// JNDI PROPERTIES.
		final Hashtable<String, Comparable> jndiProperties = new Hashtable<>();
		jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		jndiProperties.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");																											// inplaintext
		jndiProperties.put(Context.PROVIDER_URL, "remote://localhost:4447");
		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		jndiProperties.put("jboss.naming.client.ejb.context", true);
		try {
			context = new InitialContext(jndiProperties);
		} catch (NamingException e) {
			e.printStackTrace();
		}

		// Call Beans
		rc.statelessBean(context);
		rc.statefulBean(context);

	}

	private void statelessBean(Context context) {
		UsersBeanRemote statelessRemote = null;
		try {
			statelessRemote = (UsersBeanRemote) context.lookup(STATELESS_ADRESS);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		// Get User.
		Users user = statelessRemote.getUserByName("volkan");
		System.out.println(user);
		try {
			user.setBirthday(new SimpleDateFormat("yyyyMMdd").parse("19930101"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// Change Birth Date.
		statelessRemote.updateUser(user);
		System.out.println(user);

		// New Instance
		UsersBeanRemote statelessRemote2 = null;
		try {
			statelessRemote2 = (UsersBeanRemote) context.lookup(STATELESS_ADRESS);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		// Get User.
		Users user2 = statelessRemote2.getUserByName("volkan");
		System.out.println(user2);
		// Change Sex.
		user2.setSex((short) 0);
		statelessRemote2.updateUser(user2);
	}

	private void statefulBean(Context context) {

		UsersBeanRemote statefulRemote = null;
		try {
			statefulRemote = (UsersBeanRemote) context.lookup(STATEFUL_ADRESS);
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
		// Get User.
		Users user = statefulRemote.getUserByName("volkan2");
		System.out.println(user);
		try {
			user.setBirthday(new SimpleDateFormat("yyyyMMdd").parse("19930101"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// Change Birth Date.
		statefulRemote.updateUser(user);

		// New Instance
		UsersBeanRemote statefulRemote2 = null;
		try {
			statefulRemote2 = (UsersBeanRemote) context.lookup(STATEFUL_ADRESS);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		Users user2 = statefulRemote2.getUserByName("volkan2");
		System.out.println(user2);
		user2.setSex((short) 1);
		statefulRemote.updateUser(user2);
	}

}