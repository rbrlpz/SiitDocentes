package mx.itdurango.rober.siitdocentes;

public class Model {
    private String name;
    private boolean selected;
    private String score;

    public Model(String name) {
        this.name = name;
        selected = false;
        score = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
