# Generated by Django 3.2.7 on 2021-10-11 12:45

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Member',
            fields=[
                ('member_id', models.AutoField(db_column='UserID', default='', primary_key=True, serialize=False)),
                ('member_name', models.CharField(default='', max_length=10, verbose_name='MemberName')),
                ('safe_number', models.CharField(default='', max_length=20, verbose_name='SafeNumber')),
            ],
        ),
        migrations.CreateModel(
            name='Message',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('sender', models.CharField(max_length=20, verbose_name='Sender')),
                ('send_date', models.DateTimeField(auto_now_add=True, verbose_name='SendDate')),
                ('contents', models.CharField(max_length=150, verbose_name='Contents')),
                ('smishing_sort', models.BooleanField(default=False, verbose_name='SmishingSort')),
                ('member_id', models.ForeignKey(default='', on_delete=django.db.models.deletion.CASCADE, to='smishing.member')),
            ],
        ),
        migrations.CreateModel(
            name='Guide',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('guide_name', models.CharField(max_length=40, verbose_name='GuideName')),
                ('guide_number', models.CharField(max_length=45, verbose_name='GuideNumber')),
                ('member_id', models.ForeignKey(default='', on_delete=django.db.models.deletion.CASCADE, to='smishing.member')),
            ],
        ),
    ]
