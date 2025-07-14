package view;

//classe criada para guardar os temas de cores do projeto, facilitando a manutenção
import java.awt.Color;

public enum CorTema {
    FUNDO(new Color(33, 37, 41)),
    FONTE(new Color(248, 249, 250)),
    HOVER(new Color(73, 80, 87)),
    BOTAO(new Color(108, 117, 125));

    private final Color color;

    CorTema(Color color) {
        this.color = color;
    }

    public Color get() {
        return color;
    }
}
