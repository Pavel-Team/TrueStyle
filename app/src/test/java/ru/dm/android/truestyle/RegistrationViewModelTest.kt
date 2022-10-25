package ru.dm.android.truestyle

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test
import ru.dm.android.truestyle.viewmodel.RegistrationViewModel

class RegistrationViewModelTest {

    @Test
    fun checkStrongPassword() {
        assertThat(RegistrationViewModel.checkStrongPassword("abc"), `is`(0))
        assertThat(RegistrationViewModel.checkStrongPassword("abcdef"), `is`(1))
        assertThat(RegistrationViewModel.checkStrongPassword("abcdef1"), `is`(2))
        assertThat(RegistrationViewModel.checkStrongPassword("abcdefABC1"), `is`(3))
        assertThat(RegistrationViewModel.checkStrongPassword("abcdefABC1_"), `is`(4))
    }

}