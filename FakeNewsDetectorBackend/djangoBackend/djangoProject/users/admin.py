from django.contrib import admin
from django.contrib.auth.admin import UserAdmin
from users.models import CustomUser


# Register your models here.
class CustomUserAdmin(UserAdmin):
    fieldsets = UserAdmin.fieldsets + (
        (None, {'fields': ('salt',)}),
    )


admin.site.register(CustomUser, CustomUserAdmin)
