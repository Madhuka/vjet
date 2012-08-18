package org.eclipse.dltk.mod.dbgp;

public interface IDbgpRawListener {
	void dbgpPacketReceived(int sessionId, IDbgpRawPacket content);

	void dbgpPacketSent(int sessionId, IDbgpRawPacket content);
}
