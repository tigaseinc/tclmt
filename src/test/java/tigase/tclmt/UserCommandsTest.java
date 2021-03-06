/**
 * Tigase XMPP Server Command Line Management Tool - bootstrap configuration for all Tigase projects
 * Copyright (C) 2004 Tigase, Inc. (office@tigase.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. Look for COPYING file in the top folder.
 * If not, see http://www.gnu.org/licenses/.
 */
package tigase.tclmt;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import tigase.jaxmpp.core.client.exceptions.JaxmppException;
import tigase.jaxmpp.core.client.xml.Element;
import tigase.jaxmpp.core.client.xml.XMLException;
import tigase.jaxmpp.core.client.xmpp.stanzas.Stanza;
import tigase.jaxmpp.j2se.connectors.socket.StreamListener;
import tigase.jaxmpp.j2se.connectors.socket.XMPPDomBuilderHandler;
import tigase.jaxmpp.j2se.xml.J2seElement;
import tigase.xml.SimpleParser;
import tigase.xml.SingletonFactory;

/**
 * Unit test for simple App.
 */
public class UserCommandsTest
        extends TestCase {

        private static final String COMMANDS_XMLNS = "http://jabber.org/protocol/commands";
        
        private SimpleParser parser = SingletonFactory.getParserInstance();
        
        private TestConsole console = null;
        private TestSynchronizedConnection conn = null;
        private Tclmt tclmt = null;
        /**
         * Create the test case
         *
         * @param testName name of the test case
         */
        public UserCommandsTest(String testName) {
                super(testName);
        }

        @Override
        public void setUp() throws JaxmppException {
                console = new TestConsole();
                conn = new TestSynchronizedConnection();
                tclmt = new Tclmt(conn, console);
                tclmt.initialize();
                
        }
        
        @Override
        public void tearDown() {
                tclmt = null;
                try {
                        conn.disconnect();
                } catch (Exception ex) { }
                conn = null;
                console = null;                
        }
        
        /**
         * @return the suite of tests being tested
         */
        public static Test suite() {
                return new TestSuite(UserCommandsTest.class);
        }

        /**
         * Test for add-user command
         */
        public void testAddUser() throws JaxmppException {                
                String incoming = "<iq type='result' to='test@test' id='10'><command xmlns='http://jabber.org/protocol/commands' node='http://jabber.org/protocol/admin#add-user'><x xmlns='jabber:x:data'>"
                                        + "<field var='note' type='text-single'><value>Operation successful</value></field></x></command></iq>";
                
                initializeIncoming(incoming);

                tclmt.execute(new String[] { "add-user", "test@localhost", "password!", "email" });                

                List<Stanza> results = conn.getOutgoing();
                
                String consoleOut = console.toString();
                
                Stanza e = null;
                assertTrue(!results.isEmpty());
                e = results.remove(0);
                Element command = e.getChildrenNS("command", COMMANDS_XMLNS);
                assertEquals("http://jabber.org/protocol/admin#add-user", command.getAttribute("node"));
                List<Element> fields = command.getChildrenNS("x", "jabber:x:data").getChildren("field");
                assertTrue(!fields.isEmpty());
                for (Element field : fields) {
                        if ("accountjid".equals(field.getAttribute("var"))) {
                                assertEquals("test@localhost", field.getFirstChild().getValue());
                        }
                        else if ("password".equals(field.getAttribute("var"))) {
                                assertEquals("password!", field.getFirstChild().getValue());
                        }
                        else if ("email".equals(field.getAttribute("var"))) {
                                assertEquals("email", field.getFirstChild().getValue());
                        }
                }
                        
                assertTrue(consoleOut.contains("successful"));
        }

        /**
         * Test for change-user-password command
         */
        public void testChangeUserPassword() throws JaxmppException {                
                String incoming = "<iq type='result' to='test@test' id='10'><command xmlns='http://jabber.org/protocol/commands' node='http://jabber.org/protocol/admin#change-user-password'><x xmlns='jabber:x:data'>"
                                        + "<field var='note' type='text-single'><value>Operation successful</value></field></x></command></iq>";
                
                initializeIncoming(incoming);
                
                tclmt.execute(new String[] { "change-user-password", "test@localhost", "new-password!" });                

                List<Stanza> results = conn.getOutgoing();
                
                String consoleOut = console.toString();
                
                Stanza e = null;
                assertTrue(!results.isEmpty());
                e = results.remove(0);
                Element command = e.getChildrenNS("command", COMMANDS_XMLNS);
                assertEquals("http://jabber.org/protocol/admin#change-user-password", command.getAttribute("node"));
                List<Element> fields = command.getChildrenNS("x", "jabber:x:data").getChildren("field");
                assertTrue(!fields.isEmpty());
                for (Element field : fields) {
                        if ("accountjid".equals(field.getAttribute("var"))) {
                                assertEquals("test@localhost", field.getFirstChild().getValue());
                        }
                        else if ("password".equals(field.getAttribute("var"))) {
                                assertEquals("new-password!", field.getFirstChild().getValue());
                        }
                }
                
                assertTrue(consoleOut.contains("successful"));
        }        
        
        /**
         * Test for delete-user command
         */
        public void testDeleteUser() throws JaxmppException {                
                String incoming = "<iq type='result' to='test@test' id='10'><command xmlns='http://jabber.org/protocol/commands' node='http://jabber.org/protocol/admin#delete-user'><x xmlns='jabber:x:data'>"
                                        + "<field var='note' type='text-single'><value>Operation successful</value></field></x></command></iq>";
                
                initializeIncoming(incoming);
                
                tclmt.execute(new String[] { "delete-user", "test@localhost" });                

                List<Stanza> results = conn.getOutgoing();
                
                String consoleOut = console.toString();
                
                Stanza e = null;
                assertTrue(!results.isEmpty());
                e = results.remove(0);
                Element command = e.getChildrenNS("command", COMMANDS_XMLNS);
                assertEquals("http://jabber.org/protocol/admin#delete-user", command.getAttribute("node"));
                List<Element> fields = command.getChildrenNS("x", "jabber:x:data").getChildren("field");
                assertTrue(!fields.isEmpty());
                for (Element field : fields) {
                        if ("accountjids".equals(field.getAttribute("var"))) {
                                assertEquals("test@localhost", field.getFirstChild().getValue());
                        }
                }
                
                assertTrue(consoleOut.contains("successful"));
        }
        
        /**
         * Test for get-online-users-list command
         */
        public void testGetOnlineUsersList() throws JaxmppException {                
                String incoming = "<iq type='result' to='test@test' id='10'><command xmlns='http://jabber.org/protocol/commands' node='http://jabber.org/protocol/admin#get-online-users-list'><x xmlns='jabber:x:data'>"
                                        + "<field var='users' type='text-multi'><value>test1@localhost</value><value>test2@localhost</value></field></x></command></iq>";
                
                initializeIncoming(incoming);
                
                tclmt.execute(new String[] { "get-online-users-list", "localhost", "25" });                

                List<Stanza> results = conn.getOutgoing();
                
                String consoleOut = console.toString();
                
                Stanza e = null;
                assertTrue(!results.isEmpty());
                e = results.remove(0);
                Element command = e.getChildrenNS("command", COMMANDS_XMLNS);
                assertEquals("http://jabber.org/protocol/admin#get-online-users-list", command.getAttribute("node"));
                List<Element> fields = command.getChildrenNS("x", "jabber:x:data").getChildren("field");
                assertTrue(!fields.isEmpty());
                for (Element field : fields) {
                        if ("domainjid".equals(field.getAttribute("var"))) {
                                assertEquals("localhost", field.getFirstChild().getValue());
                        }
                        else if ("max_items".equals(field.getAttribute("var"))) {
                                assertEquals("25", field.getFirstChild().getValue());
                        }
                }
                
                assertTrue(consoleOut.contains("test1@localhost"));
                assertTrue(consoleOut.contains("test2@localhost"));
        }
        
        /**
         * Test for get-online-users-list command
         */
        public void testGetActiveUsersList() throws JaxmppException {                
                String incoming = "<iq type='result' to='test@test' id='10'><command xmlns='http://jabber.org/protocol/commands' node='http://jabber.org/protocol/admin#get-active-users'><x xmlns='jabber:x:data'>"
                                        + "<field var='users' type='text-multi'><value>test1@localhost</value><value>test2@localhost</value></field></x></command></iq>";
                
                initializeIncoming(incoming);
                
                tclmt.execute(new String[] { "get-active-users-list", "localhost", "25" });                

                List<Stanza> results = conn.getOutgoing();
                
                String consoleOut = console.toString();
                
                Stanza e = null;
                assertTrue(!results.isEmpty());
                e = results.remove(0);
                Element command = e.getChildrenNS("command", COMMANDS_XMLNS);
                assertEquals("http://jabber.org/protocol/admin#get-active-users", command.getAttribute("node"));
                List<Element> fields = command.getChildrenNS("x", "jabber:x:data").getChildren("field");
                assertTrue(!fields.isEmpty());
                for (Element field : fields) {
                        if ("domainjid".equals(field.getAttribute("var"))) {
                                assertEquals("localhost", field.getFirstChild().getValue());
                        }
                        else if ("max_items".equals(field.getAttribute("var"))) {
                                assertEquals("25", field.getFirstChild().getValue());
                        }
                }
                
                assertTrue(consoleOut.contains("test1@localhost"));
                assertTrue(consoleOut.contains("test2@localhost"));
        }

        /**
         * Test for get-online-users-list command
         */
        public void testGetIdleUsersList() throws JaxmppException {                
                String incoming = "<iq type='result' to='test@test' id='10'><command xmlns='http://jabber.org/protocol/commands' node='http://jabber.org/protocol/admin#get-idle-users'><x xmlns='jabber:x:data'>"
                                        + "<field var='users' type='text-multi'><value>test1@localhost</value><value>test2@localhost</value></field></x></command></iq>";
                
                initializeIncoming(incoming);
                
                tclmt.execute(new String[] { "get-idle-users-list", "localhost", "25" });                

                List<Stanza> results = conn.getOutgoing();
                
                String consoleOut = console.toString();
                
                Stanza e = null;
                assertTrue(!results.isEmpty());
                e = results.remove(0);
                Element command = e.getChildrenNS("command", COMMANDS_XMLNS);
                assertEquals("http://jabber.org/protocol/admin#get-idle-users", command.getAttribute("node"));
                List<Element> fields = command.getChildrenNS("x", "jabber:x:data").getChildren("field");
                assertTrue(!fields.isEmpty());
                for (Element field : fields) {
                        if ("domainjid".equals(field.getAttribute("var"))) {
                                assertEquals("localhost", field.getFirstChild().getValue());
                        }
                        else if ("max_items".equals(field.getAttribute("var"))) {
                                assertEquals("25", field.getFirstChild().getValue());
                        }
                }
                
                assertTrue(consoleOut.contains("test1@localhost"));
                assertTrue(consoleOut.contains("test2@localhost"));
        }
        
        public void initializeIncoming(String incoming) throws XMLException {                
                XMPPDomBuilderHandler handler = new XMPPDomBuilderHandler(new StreamListener() {

                        public void nextElement(tigase.xml.Element e) {
                                try {
                                        conn.setIncoming(Stanza.create(new J2seElement(e)));
                                } catch (JaxmppException ex) {
                                        Logger.getLogger(CompRepoCommandsTest.class.getName()).log(Level.SEVERE, null, ex);
                                }
                        }

                        public void xmppStreamClosed() {
                        }

                        public void xmppStreamOpened(Map<String, String> map) {
                        }
                        
                });

                char[] data = incoming.toCharArray();
                parser.parse(handler, data, 0, data.length);
        }
}
