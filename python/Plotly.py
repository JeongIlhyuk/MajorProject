import plotly.graph_objects as go
import pandas as pd
import numpy as np

# 3D 표면 플롯
x = np.outer(np.linspace(-2, 2, 30), np.ones(30))
y = x.copy().T
z = np.cos(x) * np.sin(y)

fig = go.Figure(data=[go.Surface(z=z, x=x, y=y)])
fig.update_layout(title='3D 표면 플롯', autosize=False,
                  width=800, height=800)
fig.write_html('3d_plot.html')  # HTML 파일로 저장
fig.show()  # 브라우저에서 표시