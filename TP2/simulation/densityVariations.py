import plotly.graph_objects as graph


def draw(iterations,dataOrder, dataOrder2, dataOrder3, dataOrder4, dataOrder5, dataOrder6):
    data = []

    data.append(graph.Scatter(
        x=list(range(1, iterations + 1)), y=dataOrder,
        mode='lines',
        name='20',
        marker={'color':'pink'}
    ))
    data.append(graph.Scatter(
        x=list(range(1, iterations + 1)), y=dataOrder2,
        mode='lines',
        name='10.204',
        marker={'color':'purple'}
    ))
    data.append(graph.Scatter(
        x=list(range(1, iterations + 1)), y=dataOrder3,
        mode='lines',
        name='5',
        marker={'color':'blue'}
    ))
    data.append(graph.Scatter(
        x=list(range(1, iterations + 1)), y=dataOrder4,
        mode='lines',
        name='2.22',
        marker={'color':'green'}
    ))
    data.append(graph.Scatter(
        x=list(range(1, iterations + 1)), y=dataOrder5,
        mode='lines',
        name='1.25',
        marker={'color':'red'}
    ))
    data.append(graph.Scatter(
        x=list(range(1, iterations + 1)), y=dataOrder6,
        mode='lines',
        name='0.8',
        marker={'color':'orange'}
    ))

    fig = graph.Figure(
        data=data,
        layout=graph.Layout(
            xaxis=dict(title='Iteraciones', titlefont=dict(size=24)),
            yaxis=dict(title='Orden', titlefont=dict(size=24)),
            legend=dict(title='Densidad', font=dict(size=24)),
            plot_bgcolor='rgba(0,0,0,0)'
        )
    )

    fig.update_layout(width=1250, height=1250)
    fig.update_xaxes(
        mirror=True,
        ticks='outside',
        showline=True,
        linecolor='black',
        tickfont=dict(size=24),
    )
    fig.update_yaxes(
        mirror=True,
        ticks='outside',
        showline=True,
        linecolor='black',
        tickfont=dict(size=24),
    )
    fig.show()

def read_input_file(filename):
    lines = []
    with open(filename) as order:
        lines = order.readlines()[2:]
    variations = []
    for line in lines:
        aux = line.split('\t')[1:2]
        variations.append(float(aux[0]))

    return len(variations), variations

if __name__ == '__main__':
    var1, iterations = read_input_file("../output/dynamicOutput_L5.txt")
    var2, iterations2 = read_input_file("../output/dynamicOutput_L7.txt")
    var3, iterations3 = read_input_file("../output/dynamicOutput_L10.txt")
    var4, iterations4 = read_input_file("../output/dynamicOutput_L15.txt")
    var5, iterations5 = read_input_file("../output/dynamicOutput_L20.txt")
    var6, iterations6 = read_input_file("../output/dynamicOutput_L25.txt")
    draw(iterations, var1, var2, var3, var4, var5, var6)

