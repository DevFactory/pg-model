package io.polyglotted.pgmodel.search;

import org.testng.annotations.Test;

import static com.google.common.collect.ImmutableList.of;
import static io.polyglotted.pgmodel.search.DocStatus.DELETED;
import static io.polyglotted.pgmodel.search.DocStatus.EXPIRED;
import static io.polyglotted.pgmodel.search.DocStatus.LIVE;
import static io.polyglotted.pgmodel.search.DocStatus.PENDING;
import static io.polyglotted.pgmodel.search.DocStatus.PENDING_DELETE;
import static io.polyglotted.pgmodel.search.DocStatus.REJECTED;
import static io.polyglotted.pgmodel.search.DocStatus.fromStatus;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DocStatusTest {

    @Test
    public void toFromStatus() throws Exception {
        for(DocStatus status : of(LIVE, EXPIRED, DELETED, PENDING, PENDING_DELETE, REJECTED)) {
            assertThat(fromStatus(status.name()), is(status));
            assertThat(fromStatus(status.toStatus()), is(status));
        }
    }
}