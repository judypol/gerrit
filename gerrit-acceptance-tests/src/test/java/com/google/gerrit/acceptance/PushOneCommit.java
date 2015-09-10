import static com.google.gerrit.acceptance.GitUtil.add;
import static com.google.gerrit.acceptance.GitUtil.amendCommit;
import static com.google.gerrit.acceptance.GitUtil.createCommit;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gerrit.acceptance.GitUtil.Commit;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidTagNameException;
import org.eclipse.jgit.api.errors.NoHeadException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

        PersonIdent i);
  private final PersonIdent i;
      @Assisted PersonIdent i) {
        db, i, SUBJECT, FILE_NAME, FILE_CONTENT);
      @Assisted("content") String content) {
        db, i, subject, fileName, content, null);
      @Nullable @Assisted("changeId") String changeId) {
    this.i = i;
  public Result to(Git git, String ref) throws GitAPIException, IOException {
    add(git, fileName, content);
    return execute(git, ref);
  public Result rm(Git git, String ref) throws GitAPIException {
    GitUtil.rm(git, fileName);
    return execute(git, ref);
  private Result execute(Git git, String ref) throws GitAPIException,
      ConcurrentRefUpdateException, InvalidTagNameException, NoHeadException {
    Commit c;
    if (changeId != null) {
      c = amendCommit(git, i, subject, changeId);
    } else {
      c = createCommit(git, i, subject);
      changeId = c.getChangeId();
      TagCommand tagCommand = git.tag().setName(tag.name);
    return new Result(ref, pushHead(git, ref, tag != null, force), c, subject);
    private final Commit commit;
    private Result(String ref, PushResult resSubj, Commit commit,
          queryProvider.get().byKeyPrefix(commit.getChangeId()));
      return commit.getChangeId();
      return commit.getCommit().getId();
      return commit.getCommit();
      assertThat(resSubj).isEqualTo(c.getSubject());
      assertThat(expectedStatus).isEqualTo(c.getStatus());
      assertThat(expectedTopic).isEqualTo(Strings.emptyToNull(c.getTopic()));
      Set<Account.Id> expectedReviewerIds =
          Sets.newHashSet(Lists.transform(Arrays.asList(expectedReviewers),
              new Function<TestAccount, Account.Id>() {
                @Override
                public Account.Id apply(TestAccount a) {
                  return a.id;
                }
              }));

      for (Account.Id accountId
          : approvalsUtil.getReviewers(db, notesFactory.create(c)).values()) {
        assertThat(expectedReviewerIds.remove(accountId))
          .named("unexpected reviewer " + accountId)
          .isTrue();
      }
      assertThat((Iterable<?>)expectedReviewerIds)
        .named("missing reviewers: " + expectedReviewerIds)
        .isEmpty();