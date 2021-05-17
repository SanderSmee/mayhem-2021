package nl.jdriven.mayhem.messages;

import ninja.robbert.mayhem.api.ActionMessage;
import ninja.robbert.mayhem.api.RegisterMessage;
import ninja.robbert.mayhem.api.StatusMessage;
import ninja.robbert.mayhem.api.WelcomeMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MsgAdapterTest {

    private MsgAdapter subject = new MsgAdapter();

    @Test
    void test() {
        var inputMessage = MsgAdapter.registerMessage();

        assertThat(inputMessage).isEqualTo(new RegisterMessage("FOobAr", "sander.smeman@jdriven.com", "yadda-yadda"));
    }

    @Test
    void readsWelcomeMessage() {
        var input = """
            {"type":"welcome","timestamp":1600000000000}""";

        assertThat(subject.fromString(input)).containsInstanceOf(WelcomeMessage.class);
    }

    @Test
    void createsActionMessage() {
        var expected = """
            {"type":"action","hero":0,"skill":0,"target":2,"override":false}""";

        assertThat(subject.toString(new ActionMessage(0, 0, 2, false))).isEqualTo(expected);
    }

    @Test
    void readsFinishedStatusMessage() {
        var input = """
            {"type":"status","status":"finished","result":"win"}""";

        assertThat(subject.fromString(input)).containsInstanceOf(StatusMessage.class);
    }
}
