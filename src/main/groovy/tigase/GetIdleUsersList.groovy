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
/*
 SCR:CommandId:get-idle-users-list
 SCR:Description:Gets list of idle users for domain
 SCR:Help:domain.com <max_items>
 Example: bin/tclmt.sh -u user@domain -p password -s node get-idle-users-list domain.com 100
 */

import tigase.tclmt.*;

def conn = connection;
def domain = (args != null && args.length > 0) ? args[0] : console.readLine("Domain:");
def max_items = "25";
if (args != null && args.length > 1) 
    max_items = args[1];

def packet = new Command(null);
packet.setAttribute("to", "sess-man@"+serverName);
Command.setNode(packet, "http://jabber.org/protocol/admin#get-idle-users");

def data = Command.getData(packet);
data.addTextSingleField('domainjid', domain);
data.addTextSingleField('max_items', max_items);


def resultPacket = conn.sendSync(packet);

data = Command.getData(resultPacket);
data.getFields().each { 
    console.writeLine it.getLabel() ?: it.getVar();
    if ("text-multi" == it.getType()) {
	def lines = it.getFieldValue();
	for (def line : lines) {
	    console.writeLine "\t" + line;
	}
    }
}
