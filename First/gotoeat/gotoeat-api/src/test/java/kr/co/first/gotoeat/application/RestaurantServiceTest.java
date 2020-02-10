package kr.co.first.gotoeat.application;

import kr.co.first.gotoeat.domain.MenuItem;
import kr.co.first.gotoeat.domain.MenuItemRepository;
import kr.co.first.gotoeat.domain.Restaurant;
import kr.co.first.gotoeat.domain.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class RestaurantServiceTest
{
    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);

        mockRestaurantRepository();
        mockMenuItemRepository();

        restaurantService = new RestaurantService(restaurantRepository, menuItemRepository);
    }

    private void mockMenuItemRepository()
    {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(MenuItem.builder()
                .name("Kimchi")
                .build());
        given(menuItemRepository.findAllByRestaurantId(1004)).willReturn(menuItems);
    }

    private void mockRestaurantRepository()
    {
        List<Restaurant> restaurants = new ArrayList<>();
        Restaurant restaurant = Restaurant.builder()
                .id(1004)
                .name("Bob zip")
                .location("Seoul")
                .build();

        restaurants.add(restaurant);
        given(restaurantRepository.findAll()).willReturn(restaurants);

        given(restaurantRepository.findById(1004)).willReturn(Optional.of(restaurant));
    }

    @Test
    public void getRestaurants()
    {
        List<Restaurant> restaurants = restaurantService.getRestaurants();

        Restaurant restaurant = restaurants.get(0);
        assertThat(restaurant.getId(), is(1004));
    }

    @Test
    public void getRestaurant()
    {
        Restaurant restaurant = restaurantService.getRestaurant(1004);

        assertThat(restaurant.getId(), is(1004));

        MenuItem menuitem = restaurant.getMenuItems().get(0);

        assertThat(menuitem.getName(), is("Kimchi"));
    }

    @Test
    public void addRestaurant()
    {

        given(restaurantRepository.save(any())).will(invocation ->
                {
                    Restaurant restaurant = invocation.getArgument(0);
                    restaurant.setId(1234);
                    return restaurant;
                }
        );

        Restaurant restaurant = Restaurant.builder()
                .name("BeRyong")
                .location("Busan")
                .build();

        Restaurant created = restaurantService.addRestaurant(restaurant);

        assertThat(created.getId(), is(1234));
    }


    @Test
    public void updateRestaurant()
    {
        Restaurant restaurant = Restaurant.builder()
                .id(1004)
                .name("Bob zip")
                .location("Seoul")
                .build();

        given(restaurantRepository.findById(1004)).willReturn(Optional.of(restaurant));

        restaurantService.updateRestaurant(1004, "Sul zip", "Busan");

        assertThat(restaurant.getName(), is("Sul zip"));
        assertThat(restaurant.getLocation(), is("Busan"));


    }
}