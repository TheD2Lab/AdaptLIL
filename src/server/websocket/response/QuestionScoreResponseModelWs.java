package server.websocket.response;

public class QuestionScoreResponseModelWs extends DataResponseModelWs {
    /**
     *     'type': 'data',
     *             'name' : 'score',
     *             'time': (new Date()).getMilliseconds(),
     *             'questionId': questionId,
     *             'score': score
     */
    protected String name;
    protected long time;
    protected Integer questionId;
    protected Integer score;

    public QuestionScoreResponseModelWs() {}

    public QuestionScoreResponseModelWs(String name, String name1, long time, Integer questionId, Integer score) {
        super(name);
        this.name = name1;
        this.time = time;
        this.questionId = questionId;
        this.score = score;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
