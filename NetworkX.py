import networkx as nx
import matplotlib.pyplot as plt

# 지도 그래프
# 그래프 생성
G = nx.Graph()
G.add_edges_from([(1, 2), (1, 3), (2, 3), (2, 4), (3, 4)])

# 최단 경로 계산 및 시각화
shortest_path = nx.shortest_path(G, source=1, target=4)
path_edges = list(zip(shortest_path, shortest_path[1:]))

pos = nx.spring_layout(G)  # 노드 위치 결정
nx.draw(G, pos, with_labels=True, node_color='lightblue')
nx.draw_networkx_edges(G, pos, edgelist=path_edges, width=3, edge_color='red')
plt.title("최단 경로 시각화")
plt.show()