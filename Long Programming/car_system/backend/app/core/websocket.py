import logging
from typing import Dict, List
from fastapi import WebSocket

logger = logging.getLogger(__name__)


class ConnectionManager:
    def __init__(self):
        self.active_connections: Dict[str, List[WebSocket]] = {}

    async def connect(self, websocket: WebSocket, client_id: str):
        await websocket.accept()
        if client_id not in self.active_connections:
            self.active_connections[client_id] = []
        self.active_connections[client_id].append(websocket)

    def disconnect(self, websocket: WebSocket, client_id: str):
        if client_id in self.active_connections:
            try:
                self.active_connections[client_id].remove(websocket)
            except ValueError:
                pass
            if not self.active_connections[client_id]:
                del self.active_connections[client_id]

    async def send_personal_message(self, message: str, client_id: str):
        if client_id not in self.active_connections:
            return
        dead: List[WebSocket] = []
        for connection in self.active_connections[client_id]:
            try:
                await connection.send_text(message)
            except Exception:
                dead.append(connection)
        for conn in dead:
            self.disconnect(conn, client_id)

    async def broadcast(self, message: str):
        dead_pairs: List[tuple] = []
        for client_id, connections in list(self.active_connections.items()):
            for connection in connections:
                try:
                    await connection.send_text(message)
                except Exception:
                    dead_pairs.append((connection, client_id))
        for conn, cid in dead_pairs:
            self.disconnect(conn, cid)

    def connection_count(self) -> int:
        return sum(len(conns) for conns in self.active_connections.values())


manager = ConnectionManager()
