**Issue **
When you open the app, it will fetch questions but it doesn't show loading indicator.
We want to fix the issue by using ViewModel, so loading bar controller needs to be handled by QuestionListViewModel.
You might need a state presenting loading status(Boolean, enum or sealed class).
