package ru.dm.android.truestyle

import android.content.Context
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import ru.dm.android.truestyle.repository.WardrobeRepository
import ru.dm.android.truestyle.util.Constants
import ru.dm.android.truestyle.viewmodel.AddClothesViewModel


class AddClothesViewModelTest {

    /*private lateinit var application: Application
    private lateinit var wardrobeRepository: WardrobeRepository
    private lateinit var subject: AddClothesViewModel

    @Mock
    private lateinit var mMockContext: Context

    @Before
    fun beforeAll() {
        application = mock(Application::class.java)
        wardrobeRepository = spy(WardrobeRepository)
        subject = AddClothesViewModel(application)
        subject.wardrobeRepository = wardrobeRepository
        Mockito.`when`(application.resources.getStringArray(R.array.seasons)).thenReturn(arrayOf("Winter", "Spring", "Summer"))
    }

    @Test
    fun getServerTitleSeason() {
        assertThat(subject.getServerTitleSeason("Зима"), `is`(Constants.SEASON_WINTER))
        assertThat(subject.getServerTitleSeason("Winter"), `is`(Constants.SEASON_WINTER))
        assertThat(subject.getServerTitleSeason("Лето"), `is`(Constants.SEASON_SUMMER))
        assertThat(subject.getServerTitleSeason("Summer"), `is`(Constants.SEASON_SUMMER))
        assertThat(subject.getServerTitleSeason("Весна"), `is`(Constants.SEASON_SPRING))
        assertThat(subject.getServerTitleSeason("Spring"), `is`(Constants.SEASON_SPRING))
        assertThat(subject.getServerTitleSeason("Осень"), `is`(Constants.SEASON_AUTUMN))
        assertThat(subject.getServerTitleSeason("Autumn"), `is`(Constants.SEASON_AUTUMN))
        assertThat(subject.getServerTitleSeason("Межсезонье"), `is`(Constants.SEASON_NAN))
        assertThat(subject.getServerTitleSeason("Inter-seasonal"), `is`(Constants.SEASON_NAN))
    }*/

}